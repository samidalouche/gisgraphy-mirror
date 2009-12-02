package com.gisgraphy.domain.repository;

import static com.gisgraphy.test.GeolocTestHelper.isFileContains;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.helper.FileHelper;
import com.gisgraphy.test.GeolocTestHelper;

public class DatabaseHelperTest extends AbstractTransactionalTestCase {

    IDatabaseHelper databaseHelper;

    /**
     * @param databaseHelper
     *            the databaseHelper to set
     */
    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
	this.databaseHelper = databaseHelper;
    }

    
    @Test
    public void testCreateNormalize_textFunctionShouldNotThrow() throws Exception {
	databaseHelper.createNormalize_textFunction();
    }
    
    @Test
    public void testdropNormalize_textFunctionShouldNotThrow() throws Exception {
	databaseHelper.dropNormalize_textFunction();
    }
    
    @Test
    public void testisNormalize_textFunctionCreatedShouldReturnTrueAfterCreation() throws Exception {
	databaseHelper.createNormalize_textFunction();
	Assert.assertTrue("after creation the Normalize_text() sql function should be created in postgres",databaseHelper.isNormalize_textFunctionCreated()); 
    }
    
    @Test
    public void testisNormalize_textFunctionCreatedShouldReturnFalseAfterDrop() throws Exception {
	databaseHelper.dropNormalize_textFunction();
	Assert.assertFalse("after deletion the Normalize_text() sql function should be deleted in postgres",databaseHelper.isNormalize_textFunctionCreated()); 
    }

    public void testExecuteFileSuccess() throws Exception {
	File tempDir = FileHelper.createTempDir(this.getClass().getSimpleName());
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

	Assert.assertTrue("The file has not been process correctly", databaseHelper.execute(new File(file.getAbsolutePath()), false).isEmpty());
	tempDir.delete();
    }

    public void testExecuteFileFailureWithContinueOnError() throws Exception {
	File tempDir = FileHelper.createTempDir(this.getClass().getSimpleName());
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

	Assert.assertFalse("The file has not been process correctly", databaseHelper.execute(new File(file.getAbsolutePath()), true).isEmpty());
	file.delete();
	tempDir.delete();
    }

    @Test
    public void testGenerateSqlCreationSchemaFileShouldCreateTheFileAndNotContainsDropInstruction() {
	File tempDir = FileHelper.createTempDir(this.getClass().getSimpleName());
	File file = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "createTables.sql");
	databaseHelper.generateSqlCreationSchemaFile(file);
	assertFalse("the creation script should not contains 'drop'", isFileContains(file, "drop"));
	assertTrue("the creation script should contains 'create'", isFileContains(file, "create"));
	file.delete();
	tempDir.delete();
    }

    @Test
    public void testGenerateSqlDropSchemaFileShouldCreateTheFileAndNotContainsCreateInstruction() {
	File tempDir = FileHelper.createTempDir(this.getClass().getSimpleName());
	File file = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "dropTables.sql");
	databaseHelper.generateSQLDropSchemaFile(file);
	assertTrue("the drop SQL script should 'drop'", isFileContains(file, "drop"));
	assertFalse("the drop SQL script should contains 'create'", isFileContains(file, "create"));
	file.delete();
	tempDir.delete();
    }

    @Test
    public void testGenerateSQLCreationSchemaFileToRerunImport() {
	File tempDir = FileHelper.createTempDir(this.getClass().getSimpleName());
	File file = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "createTablesToRerunImport.sql");
	databaseHelper.generateSQLCreationSchemaFileToRerunImport(file);
	assertFalse("The creation script to re-run import should not contains 'drop'", isFileContains(file, "drop"));
	assertTrue("The creation script to re-run import should contains 'create'", isFileContains(file, "create"));
	for (int i = 0; i < DatabaseHelper.TABLES_NAME_THAT_MUST_BE_KEPT_WHEN_RESETING_IMPORT.length; i++) {
	    assertFalse("The creation script to re-run import should not contains " + DatabaseHelper.TABLES_NAME_THAT_MUST_BE_KEPT_WHEN_RESETING_IMPORT[i], 
		    isFileContains(file, DatabaseHelper.TABLES_NAME_THAT_MUST_BE_KEPT_WHEN_RESETING_IMPORT[i]));
	}
	file.delete();
	tempDir.delete();
    }
    

    @Test
    public void testGenerateSqlDropSchemaFileToRerunImport() {
	File tempDir = FileHelper.createTempDir(this.getClass().getSimpleName());
	File file = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "dropTablesToRerunImport.sql");
	databaseHelper.generateSqlDropSchemaFileToRerunImport(file);
	assertTrue("The drop script to re-run import should contains 'drop'", isFileContains(file, "drop"));
	assertFalse("The drop script to re-run import should not contains create", isFileContains(file, "create"));
	for (int i = 0; i < DatabaseHelper.TABLES_NAME_THAT_MUST_BE_KEPT_WHEN_RESETING_IMPORT.length; i++) {
	    assertFalse("The drop script to re-run import should not contains " + DatabaseHelper.TABLES_NAME_THAT_MUST_BE_KEPT_WHEN_RESETING_IMPORT[i], 
		    isFileContains(file, DatabaseHelper.TABLES_NAME_THAT_MUST_BE_KEPT_WHEN_RESETING_IMPORT[i]));
	}
	file.delete();
	tempDir.delete();
    }
    
    @Test
    public void testGenerateSQLCreationSchemaFileToRerunImportShouldBeCoherentWithGenerateSqlDropSchemaFileToRerunImport(){
	File tempDir = FileHelper.createTempDir(this.getClass().getSimpleName());
	File fileDrop = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "dropTablesToRerunImport.sql");
	databaseHelper.generateSqlDropSchemaFileToRerunImport(fileDrop);
	
	File fileCreate = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "createTablesToRerunImport.sql");
	databaseHelper.generateSQLCreationSchemaFileToRerunImport(fileCreate);
	
	int numberOfTablesDeletion = GeolocTestHelper.countLinesInFileThatStartsWith(fileDrop, "drop table");
	int numberOfTablesCreation = GeolocTestHelper.countLinesInFileThatStartsWith(fileCreate, "create table");
	Assert.assertEquals("number of table deletion = "+numberOfTablesDeletion+" but number of tables creation="+numberOfTablesCreation,numberOfTablesCreation,numberOfTablesDeletion );
	
	int numberOfSequenceDeletion = GeolocTestHelper.countLinesInFileThatStartsWith(fileDrop, "drop sequence");
	int numberOfSequenceCreation = GeolocTestHelper.countLinesInFileThatStartsWith(fileCreate, "create sequence");
	Assert.assertEquals("number of sequence deletion = "+numberOfSequenceDeletion+" but number of sequence creation="+numberOfSequenceCreation,numberOfSequenceDeletion,numberOfSequenceCreation );
	
	fileDrop.delete();
	fileCreate.delete();
	tempDir.delete();
    }
    
    @Test
    public void testExecuteSqlDropSchemaFileToRerunImportAndCreateShouldNotThrows() throws Exception{
	File tempDir = FileHelper.createTempDir(this.getClass().getSimpleName());
	File fileDrop = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "dropTablesToRerunImport.sql");
	databaseHelper.generateSqlDropSchemaFileToRerunImport(fileDrop);
	
	File fileCreate = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "createTablesToRerunImport.sql");
	databaseHelper.generateSQLCreationSchemaFileToRerunImport(fileCreate);
	List<String> dropErrors = databaseHelper.execute(fileDrop,true);
	List<String> createErrors = databaseHelper.execute(fileCreate,true);
	Assert.assertTrue("the drop Database script has generate "+dropErrors.size()+" errors : "+printErrors(dropErrors),dropErrors.isEmpty());
	Assert.assertTrue("the create Database script has generate "+createErrors.size()+" errors : "+printErrors(createErrors),createErrors.isEmpty());
    }
    
    private String printErrors(List<String> errorsList){
    	String concatenateErrors= "";
    	for (String error: errorsList){
    		concatenateErrors = concatenateErrors + " "+ error+"\r\n\r\n";
    	}
    	return concatenateErrors;
    }
  
    @Test
    public void testGenerateSqlCreationSchemaFileThrowsIfFileIsNull() {
	try {
	    databaseHelper.generateSqlCreationSchemaFile(null);
	    fail("we should not allow creation of sql schema in a null file");
	} catch (IllegalArgumentException ignore) {
	}
    }
    
  

}
