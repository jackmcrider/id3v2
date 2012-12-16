package model;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ID3TextFrame {

	private String keyword, data;
	private byte type;
	private int size;
	private short flags;

	public ID3TextFrame(String keyword, String data, byte type, int size,
			short flags) {
		this.keyword = keyword;
		this.data = data;
		this.type = type;
		this.size = size;
		this.flags = flags;
	}

	public void setKeyword(String s) {
		this.keyword = s;
	}

	public void setData(String s) {
		if (type == 1) {
			new String(s.getBytes(), Charset.forName("UTF-16"));
			this.size = s.getBytes().length * 2 + 3;
		} else {
			this.size = s.getBytes().length + 1;
		}
		this.data = s;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public String getData() {
		return this.data;
	}

	public int getFrameBodySize() {
		return this.size;
	}

	/*
	 * 1: keyword 2: bodysize 3: flags 4: data
	 */
	public byte[] getBytes() {
		byte[] keyword = this.keyword.getBytes();
		byte[] size = ByteBuffer.allocate(4).putInt(this.size).array();
		byte[] flags = ByteBuffer.allocate(2).putShort(this.flags).array();
		byte[] data = null;

		try {
			if (type == 1)
				data = this.data.getBytes("UTF-16");
			else
				data = this.data.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		byte[] complete = new byte[10 + this.size];

		for (int i = 0; i < complete.length; i++) {
			if (i < keyword.length)
				complete[i] = keyword[i];
			if (i >= keyword.length && i < keyword.length + 4)
				complete[i] = size[i - keyword.length];
			if (i >= keyword.length + 4 && i < keyword.length + 6)
				complete[i] = flags[i - keyword.length - 4];
			if (i == keyword.length + 6)
				complete[i] = this.type;
			if (i > keyword.length + 6)
				complete[i] = data[i - keyword.length - 7];
		}

		return complete;
	}

}
