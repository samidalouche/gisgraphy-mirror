package com.gisgraphy.helper;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.test.GeolocTestHelper;


public class FileHelperTest {

    
    @Test
    public void createTempDirShouldCreateAtempDir(){
	String name = this.getClass().getSimpleName();
	File directory = FileHelper.createTempDir(name);
	Assert.assertTrue("the temporary directory should be a directory", directory.isDirectory());
	Assert.assertTrue("the temporary directory should be writable", directory.canWrite());
	Assert.assertTrue("the temporary directory haven't been created", directory.exists());
	Assert.assertTrue("the temporary directory should contains the specified parameter", directory.getName().contains(name));
	Assert.assertTrue("the tempDir has not been deleted", GeolocTestHelper.DeleteNonEmptyDirectory(directory));
    }
}
