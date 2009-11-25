package com.gisgraphy.helper;

import static com.gisgraphy.test.GeolocTestHelper.isFileContains;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.test.GeolocTestHelper;


public class FileLineFilterTest {
   @Test
public void testFilter() throws Exception{
    	File tempDir = GeolocTestHelper.createTempDir(this.getClass().getSimpleName());
	File file = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "fileToFilter.sql");
	File filteredFile = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "filteredFile.sql");
	FileOutputStream fos = null;
	OutputStreamWriter out = null;
	try {
	    fos = new FileOutputStream(file);
	    out = new OutputStreamWriter(fos, "UTF-8");

	    out.write(" sentence with filterd word number 1\r\n");
	    out.write("sentence with  second word number 2\r\n");
	    out.write("good sentence\r\n");
	    out.write("nothing\r\n");
	    out.flush();
	} finally {
	    try {
		if (fos != null) {
		    fos.flush();
		    fos.close();
		}
		if (out != null) {
		    out.flush();
		    out.close();
		}
	    } catch (Exception ignore) {
		// ignore
	    }
	}

	FileLineFilter filter = new FileLineFilter(new String[]{"filterd word", " second word"});
	filter.filter(file, filteredFile);
	Assert.assertTrue("The output file should have been created",filteredFile.exists());
	Assert.assertFalse(isFileContains(filteredFile, "second word"));
	Assert.assertFalse(isFileContains(filteredFile, "filterd word"));
	Assert.assertTrue(isFileContains(filteredFile, "nothing"));
	Assert.assertTrue(isFileContains(filteredFile, "good sentence"));
	file.delete();
	filteredFile.delete();
	tempDir.delete();
	
	
	
}

}
