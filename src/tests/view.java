package tests;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import view.*;

import control.*;

public class view
{
	private static MainControl tagger;
	private static MainWindow main;
	private static NavigationPanel navigator;
	private static EditorPanel editor;
	
	@BeforeClass
	public static void gui_aufbauen()
	{
		System.out.println("GUI erstellen");
		tagger = new MainControl();
		main = tagger.getMainWindow();
		navigator = main.getNavigationPanel();
		editor = main.getEditorPanel();
	}
	
	@AfterClass
	public static void gui_schliessen()
	{
		System.out.println("GUI schliessen");
	}
	
	@Test
	public void titel_korrekt()
	{
		assertEquals(tagger.getMainWindow().getTitle(), "ID3-Tag Editor");
	}
	
	@Test
	public void standardwerte()
	{
		assertEquals(navigator.getRoot().toString(), "mp3s");
		assertEquals(editor.getAlbum(), "Album");
		assertEquals(editor.getArtist(), "Artist");
		assertEquals(editor.getTitle(), "Title");
		assertEquals(editor.getYear(), "Year");
		assertEquals(editor.getCover(), null);
	}
}
