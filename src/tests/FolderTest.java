package tests;

import static org.junit.Assert.*;

import model.Folder;

import org.junit.Before;
import org.junit.Test;

public class FolderTest {
	
	private Folder testFiles;  
	
	private String testFilesPath = "resources/mp3s/testFiles";

	@Before
	public void setUp() throws Exception {
		testFiles = new Folder(testFilesPath, true);
	}

	@Test
	public void testFolder1() {
		assertEquals(testFiles.getChildCount(), 3);
	}
	
	@Test
	public void testFolder2() {
		assertEquals(testFiles.toString(), "testFiles");
	}

}
