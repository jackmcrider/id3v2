package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import model.Folder;
import model.MP3File;
import model.XMLReader;

import org.junit.BeforeClass;
import org.junit.Test;

import control.MainControl;
import control.Program;

/**
 * Test xml cache functionality
 * @author Karl
 *
 */
public class XMLTest {

	@SuppressWarnings("unused")
	@BeforeClass
	public static void init() {
		// Initialize program
		Program program = new Program();

		File cacheFile = new File(Program.getPath() + File.separator
				+ "resources" + File.separator + "tests" + File.separator
				+ "cache.xml");

		// Delete cache file
		if (cacheFile.exists())
			cacheFile.delete();
	}

	@Test
	/**
	 * Test behaviour with correct xmls
	 */
	public void correctXMLTests() {
		MainControl control = Program.getControl();

		control.saveAll();

		File cacheFile = new File(Program.getPath() + File.separator
				+ "resources" + File.separator + "tests" + File.separator
				+ "cache.xml");

		assertTrue(cacheFile.exists());

		try {
			XMLReader reader = new XMLReader(cacheFile);
			DefaultMutableTreeNode root = reader.readXML();

			@SuppressWarnings("unchecked")
			Enumeration<Object> children = root.children();
			while (children.hasMoreElements()) {
				Object node = children.nextElement();

				// Check that this subdirectory has 1 file in it
				if (node.toString() == "unterordner") {
					Folder f = (Folder) node;
					assertEquals(f.getChildCount(), 1);
				}

				// Check that the file has a cover as wanted
				if (node.toString() == "2.mp3") {
					MP3File m = (MP3File) node;
					assertEquals(m.hasCover(), true);
				}
			}

			// Check that there are 5 files/folders present
			assertEquals(root.getChildCount(), 5);
		} catch (Exception e) {
			fail("XML korrumpiert!!!");
		}
	}

	@Test
	/**
	 * Test behaviour with corrupt xml
	 */
	public void corruptXMLTest() {
		try {
			File cacheFile = new File(Program.getPath() + File.separator
					+ "resources" + File.separator + "corruptXML.txt");
			
			XMLReader reader = new XMLReader(cacheFile);
			reader.readXML();
			fail("Should not be correct!");
		} catch(Exception e) {
			System.out.println(e.getMessage());
			assertTrue(true);
		}
	}
	
	@Test
	/**
	 * Test behaviour with invalid xml
	 */
	public void invalidXMLTest() {
		try {
			File cacheFile = new File(Program.getPath() + File.separator
					+ "resources" + File.separator + "invalidXML.txt");
			
			XMLReader reader = new XMLReader(cacheFile);
			reader.readXML();
			fail("Should not be correct!");
		} catch(Exception e) {
			System.out.println(e.getMessage());
			assertTrue(true);
		}
	}
}
