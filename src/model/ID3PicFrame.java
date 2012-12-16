package model;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ID3PicFrame {
	String keyword, mimeType;
	int bodysize;
	byte type, dtype;
	byte[] data, description;
	short flags;
	public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
	public static final String CHARSET_UTF_16 = "UTF-16LE";
	public static final String CHARSET_UTF_16BE = "UTF-16BE";
	public static final String CHARSET_UTF_8 = "UTF-8";

	private static final String[] characterSets = { CHARSET_ISO_8859_1,
			CHARSET_UTF_16, CHARSET_UTF_16BE, CHARSET_UTF_8 };

	private static final byte[][] boms = { {}, { (byte) 0xff, (byte) 0xfe },
			{ (byte) 0xfe, (byte) 0xff }, {} };

	private static final byte[][] terminators = { { 0 }, { 0, 0 }, { 0, 0 },
			{ 0 } };

	public ID3PicFrame(String mimeType, byte type, byte dtype,
			byte[] descriptionBytes, byte[] data, String keyword, int bodysize,
			short flags) {
		this.mimeType = mimeType;
		this.type = type;
		this.dtype = dtype;
		this.description = descriptionBytes;
		this.data = data;
		this.keyword = keyword;
		this.bodysize = bodysize;
		this.flags = flags;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String s) {
		this.keyword = s;
	}

	public void setData(byte[] bytes) {

		data = bytes;

		this.mimeType = "image/jpeg";

		this.bodysize = data.length + mimeType.getBytes().length
				+ toBytes(false, false).length;

	}

	public byte[] getBytes() {
		byte[] keyword = this.keyword.getBytes();
		byte[] size = ByteBuffer.allocate(4).putInt(this.bodysize).array();
		byte[] flags = ByteBuffer.allocate(2).putShort(this.flags).array();
		byte[] data = this.getPicBytes();
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
		return complete;
	}

	protected int getLength() {
		int length = 3;
		if (mimeType != null)
			length += mimeType.length();
		if (description != null)
			length += toBytes(true, true).length;
		else
			length++;
		if (data != null)
			length += data.length;
		return length;
	}

	public byte[] getPicBytes() {
		byte[] bytes = new byte[getLength()];
		if (description != null)
			bytes[0] = dtype;
		else
			bytes[0] = 0;
		int mimeTypeLength = 0;
		if (mimeType != null && mimeType.length() > 0) {
			mimeTypeLength = mimeType.length();
			try {
				String stringToCopy = mimeType.substring(0, 0 + mimeTypeLength);
				byte[] srcBytes = stringToCopy.getBytes("ISO-8859-1");
				if (srcBytes.length > 0) {
					System.arraycopy(srcBytes, 0, bytes, 1, srcBytes.length);
				}
			} catch (UnsupportedEncodingException e) {
			}
		}
		int marker = mimeTypeLength + 1;
		bytes[marker++] = 0;
		bytes[marker++] = type;
		if (description != null && toBytes(false, false).length > 0) {
			byte[] descriptionBytes = toBytes(true, true);
			if (descriptionBytes.length > 0) {
				System.arraycopy(descriptionBytes, 0, bytes, marker,
						descriptionBytes.length);
			}
			marker += descriptionBytes.length;
		} else {
			bytes[marker++] = 0;
		}
		if (data != null && data.length > 0) {
			if (data.length > 0) {
				System.arraycopy(data, 0, bytes, marker, data.length);
			}
		}
		return bytes;

	}

	private static String characterSetForTextEncoding(byte textEncoding) {
		try {
			return characterSets[textEncoding];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Invalid text encoding "
					+ textEncoding);
		}
	}

	public byte[] toBytes(boolean includeBom, boolean includeTerminator) {
		characterSetForTextEncoding(dtype); // ensured textEncoding is valid
		int newLength = this.description.length
				+ (includeBom ? boms[dtype].length : 0)
				+ (includeTerminator ? getTerminator().length : 0);
		if (newLength == this.description.length) {
			return this.description;
		} else {
			byte bytes[] = new byte[newLength];
			int i = 0;
			if (includeBom) {
				byte[] bom = boms[dtype];
				if (bom.length > 0) {
					System.arraycopy(boms[dtype], 0, bytes, i,
							boms[dtype].length);
					i += boms[dtype].length;
				}
			}
			if (this.description.length > 0) {
				System.arraycopy(this.description, 0, bytes, i,
						this.description.length);
				i += this.description.length;
			}
			if (includeTerminator) {
				byte[] terminator = getTerminator();
				if (terminator.length > 0) {
					System.arraycopy(terminator, 0, bytes, i, terminator.length);
				}
			}
			return bytes;
		}
	}

	public byte[] getTerminator() {
		return terminators[dtype];
	}

}
