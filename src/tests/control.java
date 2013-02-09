package tests;

import static org.junit.Assert.*;

import model.MP3File;

import org.junit.BeforeClass;
import org.junit.Test;

import view.EditorPanel;
import view.MainWindow;
import view.NavigationPanel;
import control.MainControl;

public class control
{
	private static MainControl tagger;
	
	@BeforeClass
	public static void init()
	{
		tagger = new MainControl();
	}
	
	@Test
	public void test()
	{
		System.out.println(tagger.getChangedFiles());
	}

}
