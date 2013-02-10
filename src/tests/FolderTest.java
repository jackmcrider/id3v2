package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
		// teste ob Folder den Ordnernamen richtig ausgelesen hat
		assertEquals(folder.getChildAt(1).toString(), "unterordner");

		// teste ob Folder den Dateinamen richtig ausgelesen hat
		assertEquals(folder.getChildAt(0).toString(), "correct.mp3");
		assertEquals(folder.getChildAt(2).toString(), "empty.mp3");
		assertEquals(folder.getChildAt(3).toString(), "1.mp3");
		assertEquals(folder.getChildAt(4).toString(), "wrong.mp3");
		assertEquals(folder.getChildAt(1).getChildAt(0).toString(), "3.mp3");

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
