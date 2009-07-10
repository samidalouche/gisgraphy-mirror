package com.gisgraphy.helper;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.test.GeolocTestHelper;


public class UntarTest {

    @Test
    public void testUntarGzipForGzipExtension() throws IOException{
	File tempDir = GeolocTestHelper.createTempDir("targzip"+System.currentTimeMillis());
	Untar untar = new Untar("./data/tests/tar/test.tar.gz",tempDir);
	untar.untar();
	Assert.assertTrue("",new File(tempDir+File.separator+"tarfilegzip.txt").exists());
    }
    
    @Test
    public void testUntarGzipForGZExtension() throws IOException{
	File tempDir = GeolocTestHelper.createTempDir("targzip"+System.currentTimeMillis());
	Untar untar = new Untar("./data/tests/tar/test.tar.gzip",tempDir);
	untar.untar();
	Assert.assertTrue("",new File(tempDir+File.separator+"tarfilegzip.txt").exists());
    }
    
    @Test
    public void testUntarBzip2ForBZ2Extension() throws IOException{
	File tempDir = GeolocTestHelper.createTempDir("targzip"+System.currentTimeMillis());
	Untar untar = new Untar("./data/tests/tar/test.tar.bz2",tempDir);
	untar.untar();
	Assert.assertTrue("",new File(tempDir+File.separator+"tarfilebz2.txt").exists());
    }
    
    @Test
    public void testUntarBzip2ForBzip2Extension() throws IOException{
	File tempDir = GeolocTestHelper.createTempDir("targzip"+System.currentTimeMillis());
	Untar untar = new Untar("./data/tests/tar/test.tar.bzip2",tempDir);
	untar.untar();
	Assert.assertTrue("",new File(tempDir+File.separator+"tarfilebz2.txt").exists());
    }
    
    @Test (expected=RuntimeException.class)
    public void testUntarForUnknowCompression() throws IOException{
	File tempDir = GeolocTestHelper.createTempDir("targzip"+System.currentTimeMillis());
	Untar untar = new Untar("./data/tests/tar/test.tar.unknowext",tempDir);
	untar.untar();
    }
    
    @Test
    public void testUntarForNotCompressed() throws IOException{
	File tempDir = GeolocTestHelper.createTempDir("targzip"+System.currentTimeMillis());
	Untar untar = new Untar("./data/tests/tar/test.tar",tempDir);
	untar.untar();
	Assert.assertTrue("",new File(tempDir+File.separator+"tarwocompression.txt").exists());
    }
    
}
