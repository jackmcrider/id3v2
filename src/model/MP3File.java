package model;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class MP3File extends DefaultMutableTreeNode {

	private String title;
	private String artist;
	private String album;
	private String year;
	@SuppressWarnings("unused")
	private BufferedImage image;
	private ImageIcon cover;
	private File file;

	public MP3File(String path) {
		this.setUserObject(new File(path));
		parse();
	}

	public void parse() {
		try {
			boolean hasTagsLeft = true;
			File file = new File(this.getUserObject().toString());
			DataInputStream data = new DataInputStream(new FileInputStream(file));
			data.skipBytes(10);
			while (hasTagsLeft) {
				byte[] keyArr = new byte[4]; // Keyword besteht aus 4 Bytes
				data.read(keyArr);
				String keyword = new String(keyArr);
				int frameBodySize = data.readInt();
				short flags = data.readShort();
				if (frameBodySize == 0){
					hasTagsLeft = false;
					return;
				}

				byte[] textBuffer = new byte[frameBodySize];
				data.read(textBuffer);

				if (keyword.equals("TALB"))
					this.parseAlbum(textBuffer);
				if (keyword.equals("TPE1"))
					this.parseArtist(textBuffer);
				if (keyword.equals("TIT2"))
					this.parseTitle(textBuffer);
				if (keyword.equals("TYER"))
					this.parseYear(textBuffer);
				if (keyword.equals("APIC")) {
					this.parseCover(textBuffer);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void parseArtist(byte[] frame) {
		String s;
		// File f = new File()
		Byte type = frame[0];
		if (type == 1)
			s = new String(Arrays.copyOfRange(frame, 1, frame.length),
					Charset.forName("UTF-16"));
		else
			s = new String(Arrays.copyOfRange(frame, 1, frame.length));
		this.setArtist(s);
	}

	public void parseAlbum(byte[] frame) {
		String s;
		Byte type = frame[0];
		if (type == 1)
			s = new String(Arrays.copyOfRange(frame, 1, frame.length),
					Charset.forName("UTF-16"));
		else
			s = new String(Arrays.copyOfRange(frame, 1, frame.length));
		this.setAlbum(s);

	}

	public void parseTitle(byte[] frame) {
		String s;
		Byte type = frame[0];
		if (type == 1)
			s = new String(Arrays.copyOfRange(frame, 1, frame.length),
					Charset.forName("UTF-16"));
		else
			s = new String(Arrays.copyOfRange(frame, 1, frame.length));
		this.setTitle(s);
	}

	public void parseYear(byte[] frame) {
		String s;
		Byte type = frame[0];
		if (type == 1)
			s = new String(Arrays.copyOfRange(frame, 1, frame.length),
					Charset.forName("UTF-16"));
		else
			s = new String(Arrays.copyOfRange(frame, 1, frame.length));
		this.setYear(s);
	}

	public void parseCover(byte[] bytes) {
		int pointer;
		for (pointer = 1; pointer < bytes.length; pointer++) {
			if (bytes[pointer] == 0)
				break;
		}
		try {
			String mimeType = new String(bytes, 1, pointer - 1, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			String mimeType = "image/unknown";
		}
		byte pictureType = bytes[pointer + 1];
		pointer += 2;
		int pointer2;
		for (pointer2 = pointer; pointer2 < bytes.length; pointer2++) {
			if (bytes[pointer2] == 0)
				break;
		}
		int length = pointer2 - pointer;
		byte[] copy = new byte[length];
		if (length > 0) {
			System.arraycopy(bytes, pointer, copy, 0, length);
		}
		
		EncodedText description = new EncodedText(bytes[0], copy);
		pointer2 += description.getTerminator().length;
		
		length = bytes.length - pointer2;
		byte [] imageData = new byte[length];
		System.arraycopy(bytes, pointer2, imageData, 0, length);
		cover = new ImageIcon(imageData);
		this.setCover(cover);
	}

	public String toString() {
		return ((File) this.getUserObject()).getName();
	}

	public ImageIcon getCover() {
		return this.cover;
	}

	public String getTitle() {
		return this.title;
	}

	public String getArtist() {
		return this.artist;
	}

	public String getAlbum() {
		return this.album;
	}

	public String getYear() {
		return this.year;
	}

	public void setCover(ImageIcon i) {
		this.cover = i;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
