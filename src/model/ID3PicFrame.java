package model;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ID3PicFrame {
	String keyword, mimeType;
	int bodysize;
	EncodedText description;
	byte type;
	byte[] data;
	short flags;

	public ID3PicFrame(String mimeType, byte type, EncodedText description,
			byte[] data, String keyword, int bodysize, short flags) {
		this.mimeType = mimeType;
		this.type = type;
		this.description = description;
		this.data = data;
		this.keyword = keyword;
		this.bodysize = bodysize;
		this.flags = flags;
	}

	public String getKeyword() {
		return this.keyword;
	}
	public void setKeyword(String s){
		this.keyword = s;
	}
	public void setData(byte[] bytes){
		
		data = bytes;
		
		this.mimeType = "image/jpeg";
		
			this.bodysize = data.length+mimeType.getBytes().length+this.description.toBytes().length;

	}

	/*
	 * 1: keyword 2: bodysize 3: flags 4: data
	 */
	public byte[] getBytes() {
		byte[] keyword = this.keyword.getBytes();
		byte[] size = ByteBuffer.allocate(4).putInt(this.bodysize).array();
		byte[] flags = ByteBuffer.allocate(2).putShort(this.flags).array();
		byte[] data = this.getPicBytes();
		// System.out.println("l22"+data.length);
		byte[] complete = new byte[keyword.length + size.length + flags.length
				+ data.length];
		for (int i = 0; i < complete.length; i++) {
			if (i < keyword.length)
				complete[i] = keyword[i];
			if (i >= keyword.length && i < keyword.length + 4)
				complete[i] = size[i - keyword.length];
			if (i >= keyword.length + 4 && i < keyword.length + 6)
				complete[i] = flags[i - keyword.length - 4];
			if (i >= keyword.length + 6)
				complete[i] = data[i - keyword.length - 6];
		}
		// for(int i = 0; i < 20; i++)
		// System.out.println("2["+complete[i]+"]");
		return complete;
	}

	protected int getLength() {
		int length = 3;
		if (mimeType != null)
			length += mimeType.length();
		if (description != null)
			length += description.toBytes(true, true).length;
		else
			length++;
		if (data != null)
			length += data.length;
		return length;
	}

	public byte[] getPicBytes() {
		byte[] bytes = new byte[getLength()];
		if (description != null)
			bytes[0] = description.getTextEncoding();
		else
			bytes[0] = 0;
		int mimeTypeLength = 0;
		if (mimeType != null && mimeType.length() > 0) {
			mimeTypeLength = mimeType.length();
			try {
				BufferTools.stringIntoByteBuffer(mimeType, 0, mimeTypeLength,
						bytes, 1);
			} catch (UnsupportedEncodingException e) {
			}
		}
		int marker = mimeTypeLength + 1;
		bytes[marker++] = 0;
		bytes[marker++] = type;
		if (description != null && description.toBytes().length > 0) {
			byte[] descriptionBytes = description.toBytes(true, true);
			BufferTools.copyIntoByteBuffer(descriptionBytes, 0,
					descriptionBytes.length, bytes, marker);
			marker += descriptionBytes.length;
		} else {
			bytes[marker++] = 0;
		}
		if (data != null && data.length > 0) {
			BufferTools.copyIntoByteBuffer(data, 0, data.length, bytes, marker);
		}
		return bytes;
		/*
		 * byte[] mimeType = this.mimeType.getBytes(); byte[] type = new
		 * byte[1]; type[0] = this.type; byte[] des
		 * =this.description.toString().getBytes();
		 * //System.out.println("c1:"+des.length); byte[] complete = new
		 * byte[mimeType.length + type.length + des.length + this.data.length];
		 * for(int i = 0; i < complete.length; i++){ if(i < mimeType.length)
		 * complete[i] = mimeType[i]; if(i >= mimeType.length && i <
		 * mimeType.length+1) complete[i] = type[i-mimeType.length]; if(i >=
		 * mimeType.length+1 && i < mimeType.length+1+des.length) complete[i] =
		 * des[i-mimeType.length-1]; if(i >= mimeType.length+1+des.length)
		 * complete[i] = data[i-mimeType.length-des.length-1]; }
		 * //System.out.println("l2: "+data.length); return complete;
		 */
	}

}
