package tests;

import static org.junit.Assert.assertEquals;
import model.MP3File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import view.EditorPanel;
import view.MainWindow;
import view.NavigationPanel;
import control.Program;

/*
 * Hier wird die GUI gestartet und gecheckt. Da parallel irgendwie nur ein Zugriff auf eine xml-Datei funktioniert,
 * sollte hier auch die Control-Tests gemacht werden mit der bereits vorhandenen GUI.
 */
public class ViewControlTest {
	public static Program tagger;
	private static MainWindow main;
	private static NavigationPanel navigator;
	private static EditorPanel editor;
	static MP3File testFile;

	@BeforeClass
	public static void gui_aufbauen() {
		System.out.println("GUI erstellen");
		tagger = new Program();
		main = tagger.getControl().getMainWindow();
		navigator = main.getNavigationPanel();
		editor = main.getEditorPanel();
	}

	@AfterClass
	public static void gui_schliessen() {
		System.out.println("GUI schliessen");
	}

	@Test
	public void titel_korrekt() {
		assertEquals(main.getTitle(), "ID3-Tag Editor");
	}

	@Test
	public void standardwerte() {
		assertEquals(editor.getAlbum(), "Album");
		assertEquals(editor.getArtist(), "Artist");
		assertEquals(editor.getTitle(), "Title");
		assertEquals(editor.getYear(), "Year");
		assertEquals(editor.getCover(), null);
	}

	public void simulateClick(MP3File m) {
		if (m.isID3v2Tag()) {
			Program.getControl().loadMP3File(m);
		} else {
			Program.getControl().getMainWindow()
					.setStatus("This is not an MP3 file with ID3v2 tags.");
			Program.getControl().getMainWindow().getEditorPanel()
					.setCover(null);
			Program.getControl().getMainWindow().getEditorPanel()
					.setTitle("broken file");
			Program.getControl().getMainWindow().getEditorPanel()
					.setArtist("broken file");
			Program.getControl().getMainWindow().getEditorPanel()
					.setAlbum("broken file");
			Program.getControl().getMainWindow().getEditorPanel()
					.setYear("broken file");
		}
	}

	@Test
	public void clickedOnFileInTreeTest() {
		// soll ein Klick auf 1.mp3 simulieren
		testFile = new MP3File("resources/tests/1.mp3");
		this.simulateClick(testFile);
		assertEquals(editor.getAlbum(), "1");
		assertEquals(editor.getArtist(), "1");
		assertEquals(editor.getTitle(), "1");
		assertEquals(editor.getYear(), "1");
		assertEquals(editor.getCover(), null);
		testFile.setAlbum("11");
		testFile.write();

		testFile = new MP3File("resources/tests/wrong.mp3");
		this.simulateClick(testFile);
		assertEquals(editor.getAlbum(), "broken file");
		assertEquals(editor.getArtist(), "broken file");
		assertEquals(editor.getTitle(), "broken file");
		assertEquals(editor.getYear(), "broken file");
		assertEquals(editor.getCover(), null);

		testFile = new MP3File("resources/tests/1.mp3");
		this.simulateClick(testFile);
		assertEquals(editor.getAlbum(), "11");

		// aenderung wieder rueckgaengig machen
		testFile.setAlbum("1");
		testFile.write();
	}

}
