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

	public MP3File(String path) {

		this.setUserObject(new File(path));
		parse();

		this.title = ((File) this.getUserObject()).hashCode() + " title";
		this.artist = ((File) this.getUserObject()).hashCode() + " artist";
		this.album = ((File) this.getUserObject()).hashCode() + " album";
		this.year = ((File) this.getUserObject()).hashCode() + " year";
	}

	public void parse() {
		try {
			File file = new File(this.getUserObject().toString());
			DataInputStream data = new DataInputStream(
					new FileInputStream(file));

			data.skipBytes(10); // Skip the first 10 bytes
			int x = 0;
			while (x < 1090) {
				byte[] keyArr = new byte[4]; // Keyword besteht aus 4 Bytes
				data.read(keyArr);
				String keyword = new String(keyArr);
				int frameBodySize = data.readInt();
				short flags = data.readShort();
				if (frameBodySize == 0)
					return;

				System.out.println("Keyword: " + keyword);
				System.out.println("FrameBodySize: " + frameBodySize); // Frame
				System.out.println("Flags: " + flags);

				byte[] textBuffer = new byte[frameBodySize];
				data.read(textBuffer);

				/*
				 * StringBuffer buffer = new StringBuffer(); for (int i = 0; i <
				 * textBuffer.length; i++) { if (textBuffer[i] == 0) { continue;
				 * } if (keyword.startsWith("T")) { if (i < 3) { continue; } }
				 * buffer.append((char) textBuffer[i]); }
				 */

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
				x++;
				
				data.close();
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
		int marker;
		for (marker = 1; marker < bytes.length; marker++) {
			if (bytes[marker] == 0)
				break;
		}
		try {
			String mimeType = new String(bytes, 1, marker - 1, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			String mimeType = "image/unknown";
		}
		byte pictureType = bytes[marker + 1];
		marker += 2;
		int marker2;
		for (marker2 = marker; marker2 < bytes.length; marker2++) {
			if (bytes[marker2] == 0)
				break;
		}
		EncodedText description = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, marker, marker2 - marker));
		marker2 += description.getTerminator().length;
		byte [] imageData = BufferTools.copyBuffer(bytes, marker2, bytes.length - marker2);
		this.setCover(new ImageIcon(imageData));
	
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
