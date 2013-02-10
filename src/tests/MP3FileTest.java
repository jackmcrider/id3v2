package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import model.Folder;
import model.MP3File;

import org.junit.Before;
import org.junit.Test;

public class MP3FileTest {

	private MP3File correctFile;
	private MP3File wrongFile;
	private MP3File emptyFile;

	private String testFilesPath = "resources/tests/";

	@Before
	public void setUp() throws Exception {
		
		File srcFolder = new File(testFilesPath);
    	File destFolder = new File("resources/tmp/");
 
    	//make sure source exists
    	if(!srcFolder.exists()){
           System.out.println("/resources/tests/ does not exists. No JUnit-Test!");
           //just exit
           System.exit(0);
 
        }else{
 
           try{
        	copyFolder(srcFolder,destFolder);
           }catch(IOException e){
        	e.printStackTrace();
        	//error, just exit
                System.exit(0);
           }
        }
    	testFilesPath = "resources/tmp/";
		correctFile = new MP3File(testFilesPath + "1.mp3");
		wrongFile = new MP3File(testFilesPath + "wrong.mp3");
		emptyFile = new MP3File(testFilesPath + "empty.mp3");

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
	public void testeMP3ModelParser1() {
			MP3File testMP3 = new MP3File(testFilesPath + "1.mp3");
			assertEquals(testMP3.getAlbum(), "1");
			assertEquals(testMP3.getArtist(), "1");
			assertEquals(testMP3.getTitle(), "1");
			assertEquals(testMP3.getYear(), "1");
		
	}
	
	@Test
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
	
	public static void copyFolder(File src, File dest)
	    	throws IOException{
	 
	    	if(src.isDirectory()){
	 
	    		//if directory not exists, create it
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		}
	    		//list all the directory contents
	    		String files[] = src.list();
	 
	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    			File srcFile = new File(src, file);
	    			File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}
	 
	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 
	 
	    	        byte[] buffer = new byte[1024];
	 
	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }	 
	    	        in.close();
	    	        out.close();
	    	}
	    }

	

}
