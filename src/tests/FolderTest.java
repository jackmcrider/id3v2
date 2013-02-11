package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import model.Folder;
import model.MP3File;

import org.junit.BeforeClass;
import org.junit.Test;

public class FolderTest {

	static String path = "resources/tests/";
	static Folder folder;
	static MP3File testMP3;

	@BeforeClass
	public static void init() {
		File srcFolder = new File(path);
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
		folder = new Folder(destFolder.getPath(), true);
	}

	@Test
	public void testeFolderModel() {
		@SuppressWarnings("unchecked")
		Enumeration<Object> children = folder.children();
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
		assertEquals(folder.getChildCount(), 5);

	}

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
