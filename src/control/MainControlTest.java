package control;

import static org.junit.Assert.assertTrue;
import model.MP3File;

import org.junit.Before;
import org.junit.Test;

public class MainControlTest {
	private static MainControl mainControl;
	
	private String testFilePath = "resources/mp3s/testFiles/correct.mp3";
	private MP3File testFile;

	@Before
	public void setUp() throws Exception {
		mainControl = new MainControl();
		testFile = new MP3File(testFilePath);
		mainControl.loadMP3File(testFile);
	}

	@Test
	public void testGetMainWindow() {
		assertTrue(mainControl.getMainWindow().isVisible());		
	}

}
