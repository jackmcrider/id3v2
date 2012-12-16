package model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class MP3File extends DefaultMutableTreeNode {

	private byte[] descriptionBytes;
	private byte encodingType;
	private static final byte[][] terminators = { { 0 }, { 0, 0 }, { 0, 0 },
			{ 0 } };

	// Holds the header
	private byte[] header = new byte[10];
	// Holds all the tags
	private Vector<ID3TextFrame> tags = new Vector<ID3TextFrame>();

	// Holds the tags that we are able to change and the picture
	private String title;
	private String artist;
	private String album;
	private String year;
	private ID3PicFrame pframe;
	private ImageIcon cover;

	// Holds the audioPart of the MP3 file (music)
	private byte[] audioPart;

	private boolean isID3v2Tag = true;
	private boolean isParsed = false;
	private boolean isChanged = false;

	// Create MP3 file and set represented file as its user object
	public MP3File(String path) {
		this.setUserObject(new File(path));
	}

	/**
	 * Parse id3v2 tags of mp3 file
	 */
	@SuppressWarnings("resource")
	public boolean parse() {
		boolean hasTagsLeft = true;
		try {
			DataInputStream data = new DataInputStream(new FileInputStream(
					(File) this.getUserObject()));
			data.read(header);
			// Point to the file

			// Check if we have a header???
			if (data.available() < 10) {
				this.isID3v2Tag = false;
				this.setParent(null);
				data.close();
				return false;
			}

			// Read header and check if it is ID3v2
			if (Integer.toHexString(this.header[0]).equals("49")
					&& Integer.toHexString(this.header[1]).equals("44")
					&& Integer.toHexString(this.header[2]).equals("33")
					&& Integer.toHexString(this.header[3]).compareTo("FF") < 0
					&& Integer.toHexString(this.header[4]).compareTo("FF") < 0
					&& Integer.toHexString(this.header[6]).compareTo("80") < 0
					&& Integer.toHexString(this.header[7]).compareTo("80") < 0
					&& Integer.toHexString(this.header[8]).compareTo("80") < 0
					&& Integer.toHexString(this.header[9]).compareTo("80") < 0) {
				this.isID3v2Tag = true;
			} else {
				this.isID3v2Tag = false;
				this.setParent(null);
				data.close();
				return false;
			}

			while (hasTagsLeft) {
				// Read first for bytes to determine which tag it is
				byte[] keyArr = new byte[4];
				data.read(keyArr);
				// Save tag name
				String keyword = new String(keyArr);

				// Read frame body size
				int frameBodySize = data.readInt();
				// Read tag flags
				short flags = data.readShort();

				// Read audio part of the file and finish parsing
				if (frameBodySize == 0) {
					this.audioPart = new byte[data.available() + 6];
					int i = 0;
					for (i = 0; data.available() > 0; i++) {
						data.read(this.audioPart);
					}
					for (i = this.audioPart.length - 1; i >= 6; i--) {
						this.audioPart[i] = this.audioPart[i - 6];
					}
					data.close();
					hasTagsLeft = false;
					this.isParsed = true;
					return true;
				}

				// Read content of tag
				byte[] textBuffer = new byte[frameBodySize];
				data.read(textBuffer);

				// Parse text tags
				if (keyword.startsWith("T")) {
					this.parseText(textBuffer, keyword, frameBodySize, flags);
				}
				// Parse image
				if (keyword.equals("APIC")) {
					this.parseCover(textBuffer, keyword, frameBodySize, flags);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Parse text tag
	 * 
	 * @param frame
	 * @param keyword
	 * @param size
	 * @param flags
	 */
	public void parseText(byte[] frame, String keyword, int size, short flags) {
		String s;
		Byte type = frame[0];
		ID3TextFrame id3frame;

		// Check encoding of text and read accordingly
		if (type == 1) {
			s = new String(Arrays.copyOfRange(frame, 1, frame.length),
					Charset.forName("UTF-16"));

		} else {
			s = new String(Arrays.copyOfRange(frame, 1, frame.length),
					Charset.forName("ISO-8859-1"));
		}

		// Save content of tag to appropriate tag
		if (keyword.equals("TPE1"))
			this.setArtist(s);
		if (keyword.equals("TALB"))
			this.setAlbum(s);
		if (keyword.equals("TIT2"))
			this.setTitle(s);
		if (keyword.equals("TYER"))
			this.setYear(s);

		// Save
		id3frame = new ID3TextFrame(keyword, s, type, size, flags);
		tags.add(id3frame);
	}

	/**
	 * Parse cover
	 * 
	 * @param bytes
	 * @param keyword
	 * @param frameBodySize
	 * @param flags
	 */
	public void parseCover(byte[] bytes, String keyword, int frameBodySize,
			short flags) {
		int pointer;
		String mimeType;

		byte pictureType;
		byte[] imageData;

		for (pointer = 1; pointer < bytes.length; pointer++) {
			if (bytes[pointer] == 0)
				break;
		}

		try {
			if (bytes[0] == 0)
				mimeType = new String(bytes, 1, pointer - 1, "ISO-8859-1");
			else
				mimeType = new String(bytes, 1, pointer - 1, "UTF-16");
		} catch (UnsupportedEncodingException e) {
			mimeType = "image/unknown";
		}

		pictureType = bytes[pointer + 1];
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

		this.encodingType = bytes[0];
		this.descriptionBytes = copy;
		stripBomAndTerminator();
		pointer2 += getTerminator().length;

		length = bytes.length - pointer2;
		imageData = new byte[length];
		System.arraycopy(bytes, pointer2, imageData, 0, length);
		this.pframe = new ID3PicFrame(mimeType, pictureType, encodingType,
				descriptionBytes, imageData, keyword, frameBodySize, flags);
		this.cover = new ImageIcon(imageData);
		this.setCover(this.cover);
	}

	public byte[] getTerminator() {
		return terminators[encodingType];
	}

	private void stripBomAndTerminator() {
		int leadingCharsToRemove = 0;
		if (this.descriptionBytes.length >= 2
				&& ((this.descriptionBytes[0] == (byte) 0xfe && this.descriptionBytes[1] == (byte) 0xff) || (this.descriptionBytes[0] == (byte) 0xff && this.descriptionBytes[1] == (byte) 0xfe))) {
			leadingCharsToRemove = 2;
		} else if (this.descriptionBytes.length >= 3
				&& (this.descriptionBytes[0] == (byte) 0xef
						&& this.descriptionBytes[1] == (byte) 0xbb && this.descriptionBytes[2] == (byte) 0xbf)) {
			leadingCharsToRemove = 3;
		}
		int trailingCharsToRemove = 0;
		for (int i = 1; i <= 2; i++) {
			if ((this.descriptionBytes.length - leadingCharsToRemove - trailingCharsToRemove) >= i
					&& this.descriptionBytes[this.descriptionBytes.length - i] == 0) {
				trailingCharsToRemove++;
			} else {
				break;
			}
		}
		if (leadingCharsToRemove + trailingCharsToRemove > 0) {
			int newLength = this.descriptionBytes.length - leadingCharsToRemove
					- trailingCharsToRemove;
			byte[] newValue = new byte[newLength];
			if (newLength > 0) {
				System.arraycopy(this.descriptionBytes, leadingCharsToRemove,
						newValue, 0, newValue.length);
			}
			this.descriptionBytes = newValue;
		}
	}

	/**
	 * Write out changes
	 */
	public void write() {
		try {
			File f = (File) this.getUserObject();

			FileOutputStream fos = new FileOutputStream(f, false);
			DataOutputStream dos = new DataOutputStream(fos);
			BufferedOutputStream bos = new BufferedOutputStream(dos);

			for (int i = 0; i < header.length; i++) {
				// out.write(header[i]);
				bos.write(header[i]);
			}
			byte[] tag;
			for (int i = 0; i < tags.size(); i++) {
				tag = tags.get(i).getBytes();
				for (int k = 0; k < tag.length; k++) {
					// out.write(tag[k]);
					bos.write(tag[k]);
				}
			}

			tag = pframe.getBytes();
			for (int k = 0; k < tag.length; k++) {
				// out.write(tag[k]);
				bos.write(tag[k]);
			}
			for (int i = 0; i < audioPart.length; i++) {
				// out.write(rest[i]);
				bos.write(audioPart[i]);
			}
			bos.flush();
			fos.close();

			isChanged = false;

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	public String toString() {
		String name = ((File) this.getUserObject()).getName();
		if (isChanged)
			name = "*" + name;

		return name;
	}

	/**
	 * Get absolute path of file
	 * 
	 * @return
	 */
	public String getAbsolutePath() {
		return ((File) this.getUserObject()).getAbsolutePath();
	}

	/**
	 * Get specific tag of file specified by its ID3v2 keyword
	 * 
	 * @param keyword
	 * @return
	 */
	public ID3TextFrame getTag(String keyword) {
		for (int k = 0; k < tags.size(); k++) {
			if (tags.get(k).getKeyword().equals(keyword)) {
				return tags.get(k);
			}
		}

		return null;
	}

	/**
	 * Get cover of file
	 * 
	 * @return
	 */
	public ImageIcon getCover() {
		return this.cover;
	}

	/**
	 * Get title of file
	 * 
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Get artist of file
	 * 
	 * @return
	 */
	public String getArtist() {
		return this.artist;
	}

	/**
	 * Get album of file
	 * 
	 * @return
	 */
	public String getAlbum() {
		return this.album;
	}

	/**
	 * Get year of file
	 * 
	 * @return
	 */
	public String getYear() {
		return this.year;
	}

	/**
	 * Set cover of file
	 * 
	 * @param i
	 */
	public void setCover(ImageIcon i) {
		if (pframe != null && i.getIconWidth() > 0 && i.getIconHeight() > 0
				&& i != null) {
			try {
				BufferedImage buImg = new BufferedImage(i.getIconWidth(),
						i.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = buImg.createGraphics();
				g2.drawImage(i.getImage(), 0, 0, null);
				g2.dispose();
				buImg.flush();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				ImageIO.write(buImg, "png", outStream);
				outStream.flush();
				pframe.setData(outStream.toByteArray());
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.cover = i;
	}

	/**
	 * Set title of file
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		if (tags.size() > 0) {
			ID3TextFrame tag = getTag("TIT2");
			if (tag != null)
				tag.setData(title);
		}
		this.title = title;
	}

	/**
	 * Set artist of file
	 * 
	 * @param artist
	 */
	public void setArtist(String artist) {
		if (tags.size() > 0) {
			ID3TextFrame tag = getTag("TPE1");
			if (tag != null)
				tag.setData(artist);
		}
		this.artist = artist;
	}

	/**
	 * Set album of file
	 * 
	 * @param album
	 */
	public void setAlbum(String album) {
		if (tags.size() > 0) {
			ID3TextFrame tag = getTag("TALB");
			if (tag != null)
				tag.setData(album);
		}
		this.album = album;
	}

	/**
	 * Set year of file
	 * 
	 * @param year
	 */
	public void setYear(String year) {
		if (tags.size() > 0) {
			ID3TextFrame tag = getTag("TYER");
			if (tag != null)
				tag.setData(year);
		}
		this.year = year;
	}

	/**
	 * Check if this file contains ID3v2 tags
	 * 
	 * @return
	 */
	public boolean isID3v2Tag() {
		return this.isID3v2Tag;
	}

	/**
	 * Check if the file is parsed
	 * 
	 * @return
	 */
	public boolean isParsed() {
		return this.isParsed;
	}

	public void changed() {
		isChanged = true;
	}
}
