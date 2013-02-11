package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import model.MP3File;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the MP3File model
 * @author Karl
 *
 */
public class MP3FileTest {

	private String testFilesPath;

	@Before
	public void setUp() throws Exception {

		testFilesPath = "resources/tests/";

		File srcFolder = new File(testFilesPath);
		File destFolder = new File("resources/tmp/");

		// make sure source exists
		if (!srcFolder.exists()) {
			System.out
					.println("/resources/tests/ does not exists. No JUnit-Test!");
			// just exit
			System.exit(0);

		} else {

			try {
				copyFolder(srcFolder, destFolder);
			} catch (IOException e) {
				e.printStackTrace();
				// error, just exit
				System.exit(0);
			}
		}
		testFilesPath = "resources/tmp/";

	}

	@Test
	/**
	 * Test parsing
	 */
	public void testParse2() {
		MP3File testFile = new MP3File(testFilesPath + "wrong.mp3");
		assertTrue(!testFile.parse());
	}

	@Test
	/**
	 * Test parsing
	 */
	public void testParse3() {
		MP3File testFile = new MP3File(testFilesPath + "empty.mp3");
		assertTrue(!testFile.parse());
	}

	@Test
	/**
	 * Test parsing
	 */
	public void testeMP3ModelParser1() {
		MP3File testMP3 = new MP3File(testFilesPath + "1.mp3");
		assertEquals(testMP3.getAlbum(), "1");
		assertEquals(testMP3.getArtist(), "1");
		assertEquals(testMP3.getTitle(), "1");
		assertEquals(testMP3.getYear(), "1");
	}

	@Test
	/**
	 * Test writing/parsing
	 */
	public void testeMP3ModelWriter1() {
		MP3File testMP3 = new MP3File(testFilesPath + "1.mp3");
		testMP3.setAlbum("album1");
		testMP3.setTitle("title1");
		testMP3.setArtist("artist1");
		testMP3.setYear("11111111");
		testMP3.write();
		testMP3.parse();
		assertEquals(testMP3.getAlbum(), "album1");
		assertEquals(testMP3.getArtist(), "artist1");
		assertEquals(testMP3.getTitle(), "title1");
		assertEquals(testMP3.getYear(), "1111");
	}

	@Test
	/**
	 * Test removing of cover
	 */
	public void removeCover() {
		MP3File m1;
		MP3File m2;

		m1 = new MP3File(testFilesPath + "2.mp3");
		assertEquals(m1.hasCover(), true);
		m1.setHasCover(false);
		m1.write();

		m2 = new MP3File(testFilesPath + "2.mp3");
		assertEquals(m2.hasCover(), false);
	}

	@Test
	/**
	 * Test adding a cover
	 */
	public void addCover() {
		MP3File m1 = new MP3File(testFilesPath + "1.mp3");
		File file = new File(testFilesPath + "bild.jpg");
		BufferedImage image;
		ImageIcon icon;
		try {
			image = ImageIO.read(file);
			icon = new ImageIcon(image.getScaledInstance(100, 100,
					Image.SCALE_SMOOTH));

			m1.setCover(icon);
			m1.write();
		} catch (IOException ex) {
			fail("Could not write cover!");
		}
		
		MP3File m2 = new MP3File(testFilesPath + "1.mp3");
		assertEquals(m2.hasCover(), true);
	}

	/**
	 * Copy a complete folder
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
			}
			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		}
	}

}
