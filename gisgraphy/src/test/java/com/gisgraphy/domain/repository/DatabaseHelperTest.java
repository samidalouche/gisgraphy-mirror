package com.gisgraphy.domain.repository;

import java.io.File;


public class DatabaseHelperTest extends AbstractTransactionalTestCase {
	
	DatabaseHelper databaseHelper ;
	
	/**
	 * @param databaseHelper the databaseHelper to set
	 */
	public void setDatabaseHelper(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	public void testExecuteFile() throws Exception {
		databaseHelper.execute(new File("/home/david/Bureau/dist2/sql/resetdb.sql"));
	}

}
