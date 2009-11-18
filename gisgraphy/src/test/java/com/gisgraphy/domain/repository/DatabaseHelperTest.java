package com.gisgraphy.domain.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import junit.framework.Assert;

import com.gisgraphy.test.GeolocTestHelper;

public class DatabaseHelperTest extends AbstractTransactionalTestCase {

    DatabaseHelper databaseHelper;

    /**
     * @param databaseHelper
     *            the databaseHelper to set
     */
    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
	this.databaseHelper = databaseHelper;
    }

    public void testExecuteFileSuccess() throws Exception {
	File tempDir = GeolocTestHelper.createTempDir(this.getClass().getSimpleName());
	File file = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "sqlErrorFile.sql");
	FileOutputStream fos = null;
	OutputStreamWriter out = null;
	try {
	    fos = new FileOutputStream(file);
	    out = new OutputStreamWriter(fos, "UTF-8");
	    
	    out.write("\\connect psql command that should be ignore\r\n");
	    out.write("create table test_Table (id int8 not null);\r\n");
	    out.write("-- comment \r\n");
	    out.write("drop table test_Table;\r\n");
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

	Assert.assertTrue("The file has not been process corectly",databaseHelper.execute(new File(file.getAbsolutePath()), false));
    }
    
    public void testExecuteFileFailureWithContinueOnError() throws Exception {
	File tempDir = GeolocTestHelper.createTempDir(this.getClass().getSimpleName());
	File file = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "sqlErrorFile.sql");
	FileOutputStream fos = null;
	OutputStreamWriter out = null;
	try {
	    fos = new FileOutputStream(file);
	    out = new OutputStreamWriter(fos, "UTF-8");
	    
	    out.write("create table test_Table (id int8 not null);\r\n");
	    out.write("-- comment \r\n");
	    out.write("do something postgres don't understand\r\n");
	    out.write("drop table test_Table;\r\n");
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

	Assert.assertFalse("The file has not been process corectly",databaseHelper.execute(new File(file.getAbsolutePath()), true));
    }
    

}
