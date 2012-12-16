package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MP3FileTest {

	private MP3File correctFile;
	private MP3File wrongFile;
	private MP3File emptyFile;

	private String testFilesPath = "resources/mp3s/testFiles/";

	private String correctFileTitle = "Turtle";
	private String correctFileArtist = "The Wind Whistles";
	private String correctFileAlbum = "Animals Are People Too";
	private String correctFileYear = "2009";

	@Before
	public void setUp() throws Exception {
		correctFile = new MP3File(testFilesPath + "correct.mp3");
		wrongFile = new MP3File(testFilesPath + "wrong.mp3");
		emptyFile = new MP3File(testFilesPath + "empty.mp3");

		correctFile.parse();
		wrongFile.parse();
		emptyFile.parse();
	}

	@Test
	public void testParse1() {
		MP3File testFile = new MP3File(testFilesPath + "correct.mp3");
		assertTrue(testFile.parse());
	}

	@Test
	public void testParse2() {
		MP3File testFile = new MP3File(testFilesPath + "wrong.mp3");
		assertTrue(!testFile.parse());
	}

	@Test
	public void testParse3() {
		MP3File testFile = new MP3File(testFilesPath + "empty.mp3");
		assertTrue(!testFile.parse());
	}

	@Test
	public void testGetTitle1() {
		assertEquals(correctFile.getTitle(), correctFileTitle);
	}

	@Test
	public void testGetTitle2() {
		assertEquals(wrongFile.getTitle(), null);
	}

	@Test
	public void testGetArtist1() {
		assertEquals(correctFile.getArtist(), correctFileArtist);
	}

	@Test
	public void testGetArtist2() {
		assertEquals(wrongFile.getArtist(), null);
	}

	@Test
	public void testGetAlbum1() {
		assertEquals(correctFile.getAlbum(), correctFileAlbum);
	}

	@Test
	public void testGetAlbum2() {
		assertEquals(wrongFile.getAlbum(), null);
	}

	@Test
	public void testGetYear1() {
		assertEquals(correctFile.getYear(), correctFileYear);
	}

	@Test
	public void testGetYear2() {
		assertEquals(wrongFile.getYear(), null);
	}

	@Test
	public void testIsID3v2Tag1() {
		assertTrue(correctFile.isID3v2Tag());
	}

	@Test
	public void testIsID3v2Tag2() {
		assertTrue(!wrongFile.isID3v2Tag());
	}

	@Test
	public void testIsID3v2Tag3() {
		assertTrue(!emptyFile.isID3v2Tag());
	}

	@Test
	public void testIsParsed1() {
		assertTrue(correctFile.isParsed());
	}

	@Test
	public void testIsParsed2() {
		assertTrue(!wrongFile.isParsed());
	}

	@Test
	public void testIsParsed3() {
		assertTrue(!emptyFile.isParsed());
	}

}
