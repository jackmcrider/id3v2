package model;

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

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class MP3File extends DefaultMutableTreeNode {
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

	// Holds the rest of the MP3 file (music)
	private byte[] rest;
	
	private boolean isID3v2Tag = true;
	
	// Create MP3 file and set represented file as its user object
	public MP3File(String path) {
		this.setUserObject(new File(path));
		this.parse();
	}

	public void parse() {
		boolean hasTagsLeft = true;
		try {
			// Point to the file
			DataInputStream data = new DataInputStream(new FileInputStream(
					(File) this.getUserObject()));
			System.out.println("dataSize_before:" + data.available());
			
			// Check if we have a header
			if(data.available() < 10){
				this.isID3v2Tag = false;
				return;
			}

			// Read header and check if it is ID3v2
			data.read(this.header);
			if (Integer.toHexString(this.header[0]).equals("49")
					&& Integer.toHexString(this.header[1]).equals("44")
					&& Integer.toHexString(this.header[2]).equals("33")
					&& Integer.toHexString(this.header[3]).compareTo("FF") < 0
					&& Integer.toHexString(this.header[4]).compareTo("FF") < 0
					&& Integer.toHexString(this.header[6]).compareTo("80") < 0
					&& Integer.toHexString(this.header[7]).compareTo("80") < 0
					&& Integer.toHexString(this.header[8]).compareTo("80") < 0
					&& Integer.toHexString(this.header[9]).compareTo("80") < 0) {
				System.out.println(this.getUserObject().toString()+" is ID3v2!");
			} else {
				this.isID3v2Tag = false;
				return;
			}

			int x = 0;
			while (hasTagsLeft) {
				byte[] keyArr = new byte[4];

				data.read(keyArr);
				String keyword = new String(keyArr);

				int frameBodySize = data.readInt();
				short flags = data.readShort();

				if (frameBodySize == 0) {
					rest = new byte[data.available() + 6];
					for (int i = 0; data.available() > 0; i++) {
						data.read(rest);
					}
					// rest = this.shiftZeros(6,rest);
					System.out.println("audioSize_before:" + rest.length);
					data.close();
					hasTagsLeft = false;
					return;
				}

				byte[] textBuffer = new byte[frameBodySize];
				data.read(textBuffer);
				if (x == 3) {
					System.out.println(keyword);
					System.out.println(frameBodySize);
				}

				if (keyword.startsWith("T")) {
					System.out.println("tagSize_before" + x + ":"
							+ (textBuffer.length + 10));
					this.parseText(textBuffer, keyword, frameBodySize, flags);
				}
				if (keyword.equals("APIC")) {
					System.out.println("picSize_before:"
							+ (textBuffer.length + 10));
					this.parseCover(textBuffer, keyword, frameBodySize, flags);
				}
				x++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public byte[] shiftZeros(int amount, byte[] data) {
		byte[] tmp = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			if (i < amount)
				tmp[i] = 0;
			else
				tmp[i] = data[i - amount];
		}
		return tmp;
	}

	public void parseText(byte[] frame, String keyword, int size, short flags) {
		String s;
		Byte type = frame[0];
		ID3TextFrame id3frame;
		if (type == 1) {
			s = new String(Arrays.copyOfRange(frame, 1, frame.length), Charset
					.forName("UTF-16"));

		} else {
			s = new String(Arrays.copyOfRange(frame, 1, frame.length), Charset
					.forName("ISO-8859-1"));

		}
		if (keyword.equals("TPE1"))
			this.setArtist(s);
		if (keyword.equals("TALB"))
			this.setAlbum(s);
		if (keyword.equals("TIT2"))
			this.setTitle(s);
		if (keyword.equals("TYER"))
			this.setYear(s);
		id3frame = new ID3TextFrame(keyword, s, type, size, flags);
		tags.add(id3frame);
	}

	public void parseCover(byte[] bytes, String keyword, int frameBodySize,
			short flags) {
		// for(int i = 0; i < keyword.getBytes().length; i++)
		// System.out.println("1k["+keyword.getBytes()[i]+"]");
		//		
		// for(int i = 0; i < 4; i++)
		// System.out.println("1s["+ByteBuffer.allocate(4).putInt(frameBodySize).array()[i]+"]");
		//		
		// for(int i = 0; i < 2; i++)
		// System.out.println("1f["+ByteBuffer.allocate(2).putShort(flags).array()[i]+"]");
		//		
		// for(int i = 0; i < (20-6-keyword.getBytes().length); i++)
		// System.out.println("1["+bytes[i]+"]");
		//		
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
		System.out.println(bytes[0]);
		System.out.println(mimeType);
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
		// System.out.println("c:"+copy.length);
		EncodedText description = new EncodedText(bytes[0], copy);
		pointer2 += description.getTerminator().length;

		length = bytes.length - pointer2;
		imageData = new byte[length];
		System.arraycopy(bytes, pointer2, imageData, 0, length);
		pframe = new ID3PicFrame(mimeType, pictureType, description, imageData,
				keyword, frameBodySize, flags);
		cover = new ImageIcon(imageData);
		this.setCover(cover);
	}

	public void write() {
		try {
			File f = new File(this.getUserObject().toString());
			FileOutputStream fos = new FileOutputStream(f, false);
			DataOutputStream dos = new DataOutputStream(fos);
			// FileWriter fstream = new
			// FileWriter(this.getUserObject().toString(),false);
			// BufferedWriter out = new BufferedWriter(fstream);
			for (int i = 0; i < header.length; i++) {
				// out.write(header[i]);
				dos.write(header[i]);
			}
			byte[] tag;
			for (int i = 0; i < tags.size(); i++) {
				tag = tags.get(i).getBytes();
				System.out.println("tagSize_after" + i + ":" + tag.length);
				for (int k = 0; k < tag.length; k++) {
					// out.write(tag[k]);
					dos.write(tag[k]);
				}

			}

			tag = pframe.getBytes();
			System.out.println("picSize_after:" + tag.length);
			// System.out.println("p1:"+tag.length);
			for (int k = 0; k < tag.length; k++) {
				// out.write(tag[k]);
				dos.write(tag[k]);
			}
			System.out.println("audioSize_after:" + rest.length);
			for (int i = 0; i < rest.length; i++) {
				// System.out.println(i+"/"+rest.length);
				// out.write(rest[i]);
				dos.write(rest[i]);
			}
			System.out.println("size_after:" + dos.size());

			dos.flush();
			dos.close();
			fos.close();

			// out.flush();
			// out.close();
			// fstream.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
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
		/*
		 * ID3TextFrame tag = null; int i = 0; if(tags.size() > 0){ tag =
		 * this.tags.get(i); while(!tag.getKeyword().equals("TPE1")){ i++; tag =
		 * tags.get(i); } } tag.setData(title);
		 */
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
	
	public boolean isID3v2Tag() {
		return this.isID3v2Tag;
	}
}
