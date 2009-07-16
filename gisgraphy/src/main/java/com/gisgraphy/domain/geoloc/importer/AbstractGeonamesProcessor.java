/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
/**
 *
 */
package com.gisgraphy.domain.geoloc.importer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.gisgraphy.domain.repository.GisFeatureDao;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.ImporterStatus;

/**
 * Base class for all geonames processor. it provides session management and the
 * ability to process one or more CSV file
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public abstract class AbstractGeonamesProcessor implements IGeonamesProcessor {
    protected int totalReadLine = 0;
    protected int readFileLine = 0;
    protected String statusMessage = "";

    protected ImporterStatus status = ImporterStatus.UNPROCESSED;

    /**
     * @see IGeonamesProcessor#getNumberOfLinesToProcess()
     */
    int numberOfLinesToProcess = 0;

    /**
     * This fields is use to generate unique featureid when importing features
     * because we don't know yet the featureId and this field is required. it
     * should be multiply by -1 to be sure that it is not in conflict with the
     * Geonames one which are all positive
     * 
     * @see GisFeatureDao#getDirties()
     */
    static Long nbGisInserted = 0L;

    protected ImporterConfig importerConfig;

    /**
     * The logger
     */
    protected static final Logger logger = LoggerFactory
	    .getLogger(AbstractGeonamesProcessor.class);

    private File[] filesToProcess;

    /**
     * Lines starting with this prefix are considered as comments
     */
    protected String COMMENT_START = "#";

    private boolean hasConsumedFirstLine = false;

    /**
     * Whether the end of the document has been reached
     */
    private boolean endOfDocument = false;

    /**
     * The bufferReader for the current read Geonames file
     */
    protected BufferedReader in;

    /**
     * The transaction manager
     */
    private PlatformTransactionManager transactionManager;

    /**
     * Template Method : Whether the processor should ignore the first line of
     * the input
     * 
     * @return true if the processor should ignore first line
     */
    protected abstract boolean shouldIgnoreFirstLine();

    /**
     * Should flush and clear all the Daos that are used by the processor. This
     * avoid memory leak
     */
    protected abstract void flushAndClear();

    /**
     * Will flush after every commit
     * 
     * @see #flushAndClear()
     */
    protected abstract void setCommitFlushMode();

    private TransactionStatus txStatus = null;

    private DefaultTransactionDefinition txDefinition;

    /**
     * @return the number of fields the processed Geonames file should have
     */
    protected abstract int getNumberOfColumns();

    /**
     * Whether the filter should ignore the comments (i.e. lines starting with #)
     * 
     * @see AbstractGeonamesProcessor#COMMENT_START
     */
    protected abstract boolean shouldIgnoreComments();

    /**
     * Whether we should consider the line as as comment or not (i.e. : it
     * doesn't start with {@link #COMMENT_START})
     * 
     * @param input
     *                the line we want to know if it is a commented line
     * @return true is the specified line is a commented line
     */
    private boolean isNotComment(String input) {
	return (!shouldIgnoreComments())
		|| (shouldIgnoreComments() && !input.startsWith(COMMENT_START));
    }

    /**
     * Default constructor
     */
    public AbstractGeonamesProcessor() {
	super();
    }

    /**
     * The current processed file
     */
    protected File currentFile;

    /**
     * Template method that can be override. This method is called before the
     * process start. it is not called for each file processed.
     */
    protected void setup() {
    }

    /**
     * @return The files to be process
     * @see ImporterHelper
     */
    protected abstract File[] getFiles();

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#getCurrentFile()
     */
    public String getCurrentFileName() {

	if (this.currentFile != null) {
	    return this.currentFile.getName();
	}
	return "?";
    }

    /**
     * Process the line if needed (is not a comment, should ignore first line,
     * is end of document,...)
     * 
     * @return The number of lines that have been processed for the current
     *         processed file
     * @throws GeonamesProcessorException
     *                 if an error occurred
     */
    public int readLineAndProcessData() throws GeonamesProcessorException {
	if (this.isEndOfDocument()) {
	    throw new IllegalArgumentException(
		    "Must NOT be called when it is the end of the document");
	}

	String input;
	try {
	    input = (this.in).readLine();
	} catch (IOException e1) {
	    throw new GeonamesProcessorException("can not read line ", e1);
	}

	if (input != null) {
	    readFileLine++;
	    if (isNotComment(input)) {
		if (this.shouldIgnoreFirstLine() && !hasConsumedFirstLine) {
		    hasConsumedFirstLine = true;
		} else {
		    try {
			this.processData(input);
		    } catch (MissingRequiredFieldException mrfe) {
			if (this.importerConfig.isMissingRequiredFieldThrows()) {
			    logger.error("A requrired field is missing "
				    + mrfe.getMessage());
			    throw new GeonamesProcessorException(
				    "A requrired field is missing "
					    + mrfe.getMessage(), mrfe);
			} else {
			    logger.warn(mrfe.getMessage());
			}
		    } catch (WrongNumberOfFieldsException wnofe) {
			if (this.importerConfig.isWrongNumberOfFieldsThrows()) {
			    logger
				    .error("wrong number of fields during import "
					    + wnofe.getMessage());
			    throw new GeonamesProcessorException(
				    "Wrong number of fields during import "
					    + wnofe.getMessage(), wnofe);
			} else {
			    logger.warn(wnofe.getMessage());
			}
		    } catch (Exception e) {
			e.printStackTrace();
			logger.error("An Error occurred on Line "
				+ readFileLine + " for " + input + " : "
				+ e.getCause());
			throw new GeonamesProcessorException(
				"An Error occurred on Line " + readFileLine
					+ " for " + input + " : "
					+ e.getCause(), e);
		    }
		}
	    }

	} else {
	    this.endOfDocument = true;
	}
	return readFileLine;
    }

    /**
     * Process a read line of the geonames file, must be implemented by the
     * concrete class
     * 
     * @param line
     *                the line to process
     */
    protected abstract void processData(String line)
	    throws GeonamesProcessorException;

    /**
     * Manage the transaction, flush Daos, and process all files to be processed
     */
    public void process() {
	try {
	    if (shouldBeSkipped()){
		this.status = ImporterStatus.SKIPPED;
		return;
	    }
	    this.status = ImporterStatus.PROCESSING;
	    setup();
	    this.filesToProcess = getFiles();
	    if (this.filesToProcess.length == 0) {
		logger.info("there is 0 file to process for "
			+ this.getClass().getSimpleName());
		return;
	    }
	    for (int i = 0; i < filesToProcess.length; i++) {
		currentFile = filesToProcess[i];
		this.endOfDocument = false;
		getBufferReader(filesToProcess[i]);
		processFile();
		closeBufferReader();

	    }
	    
	this.status = ImporterStatus.PROCESSED;
	} catch (Exception e) {
	    e.printStackTrace();
	    this.status = ImporterStatus.ERROR;
	    String fileName = "Unknow";
	    if (currentFile != null){
		currentFile.getName();
	    }
	    this.statusMessage = "An error occurred when processing "
		    + this.getClass().getSimpleName() + " on file "
		    + fileName + " on line " + getReadFileLine()
		    + " : " + e.getCause();
	    logger.error(statusMessage);
	    throw new GeonamesProcessorException(statusMessage, e.getCause());
	} finally {
	    try {
		tearDown();
	    } catch (Exception e) {
		this.status = ImporterStatus.ERROR;
		logger.error("An error occured on teardown :"+e);
	    }
	}
    }


    /**
     * @return true if the processor should Not be executed
     */
    protected boolean shouldBeSkipped() {
	return false;
    }

    private void getBufferReader(File file) {
	InputStream inInternal = null;
	// uses a BufferedInputStream for better performance
	try {
	    inInternal = new BufferedInputStream(new FileInputStream(file));
	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	}

	try {
	    this.in = new BufferedReader(new InputStreamReader(inInternal,
		    Constants.CHARSET));
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException(e);
	}
    }

    private void processFile() throws GeonamesProcessorException {
	try {
	    hasConsumedFirstLine = false;
	    readFileLine = 0;
	    logger.info("will process " + currentFile.getName());
	    // Transaction Definition
	    txDefinition = new DefaultTransactionDefinition();
	    txDefinition
		    .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

	    txStatus = transactionManager.getTransaction(txDefinition);
	    setCommitFlushMode();
	    while (!isEndOfDocument()) {
		this.readLineAndProcessData();
		totalReadLine++;
		if (totalReadLine % this.getMaxInsertsBeforeFlush() == 0) {
		    logger
			    .info("maxInsertsBeforeFlush reached, flushing and clearing: "
				    + totalReadLine);
		    // and commit !
		    commit(txStatus);
		    // and re-opens a new transaction
		    txStatus = transactionManager.getTransaction(txDefinition);
		    setCommitFlushMode();

		}
	    }
	    commit(txStatus);
	    totalReadLine--;// remove a processed line because it has been
	    // incremented on time more
	} catch (Exception e) {
	    transactionManager.rollback(txStatus);
	    throw new GeonamesProcessorException(
		    "An error occurred when processing "
			    + currentFile.getName() + " on line "
			    + readFileLine + " : " + e.getCause(), e.getCause());
	}
    }

    /**
     * Template method that can be override. This method is called after the end
     * of the process. it is not called for each file processed.
     */
    protected void tearDown() {
	closeBufferReader();
    }

    private void closeBufferReader() {
	if (in != null) {
	    try {
		in.close();
	    } catch (IOException e) {

	    }
	}
    }

    private void commit(TransactionStatus txStatus) {
	flushAndClear();
	transactionManager.commit(txStatus);
    }

    /**
     * Check that the array is not null, and the fields of the specified
     * position is not empty (after been trimed)
     * 
     * @param fields
     *                The array to test
     * @param position
     *                the position of the field to test in the array
     * @param required
     *                if an exception should be thrown if the field is empty
     * @return true is the field of the specifed position is empty
     * @throws MissingRequiredFieldException
     *                 if the fields is empty and required is true
     */
    protected static boolean isEmptyField(String[] fields, int position,
	    boolean required) {
	if (fields == null) {
	    throw new MissingRequiredFieldException(
		    "can not chek fields if the array is null");
	}
	if (position < 0) {
	    throw new MissingRequiredFieldException(
		    "position can not be < 0 => position = " + position);
	}
	if (fields.length == 0) {
	    throw new MissingRequiredFieldException("fields is empty");
	}
	if (position > (fields.length - 1)) {

	    if (!required) {
		return true;
	    } else {
		throw new MissingRequiredFieldException("fields has "
			+ (fields.length)
			+ " element(s), can not get element with position "
			+ (position) + " : " + dumpFields(fields));
	    }

	}
	String string = fields[position];
	if (string != null && string.trim().equals("")) {
	    if (!required) {
		return true;
	    } else {
		throw new MissingRequiredFieldException("fields[" + position
			+ "] is required for featureID " + fields[0] + " : "
			+ dumpFields(fields));
	    }
	}
	return false;

    }

    /**
     * @param fields
     *                The array to process
     * @return a string which represent a human readable string of the Array
     */
    protected static String dumpFields(String[] fields) {
	String result = "[";
	for (String element : fields) {
	    result = result + element + ";";
	}
	return result + "]";
    }

    /**
     * Utility method which throw an exception if the number of fields is not
     * the one expected (retrieved by {@link #getNumberOfColumns()})
     * 
     * @see #getNumberOfColumns()
     * @param fields
     *                The array to check
     */
    protected void checkNumberOfColumn(String[] fields) {
	if (fields.length != getNumberOfColumns()) {

	    throw new WrongNumberOfFieldsException(
		    "The number of fields is not correct. expected : "
			    + getNumberOfColumns() + ", founds :  "
			    + fields.length, new Throwable(
			    "The number of fields is not correct. expected : "
				    + getNumberOfColumns() + ", founds :  "
				    + fields.length));
	}

    }

    /**
     * @return true if the end of the document for the current processed file is
     *         reached
     */
    protected boolean isEndOfDocument() {
	return endOfDocument;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#getReadFileLine()
     */
    public int getReadFileLine() {
	return this.readFileLine;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#getTotalReadedLine()
     */
    public int getTotalReadLine() {
	return this.totalReadLine;
    }

    @Required
    public void setTransactionManager(
	    PlatformTransactionManager transactionManager) {
	this.transactionManager = transactionManager;
    }

    @Required
    public void setImporterConfig(ImporterConfig importerConfig) {
	this.importerConfig = importerConfig;
    }

    /**
     * @return the number of line to process
     */
    protected int countLines() {
	int lines = 0;
	BufferedReader br = null;
	File[] files = getFiles();
	BufferedInputStream bis = null;
	for (int i = 0; i < files.length; i++) {
	    File countfile = files[i];
	    try {
		bis = new BufferedInputStream(new FileInputStream(countfile));
		br = new BufferedReader(new InputStreamReader(bis,
			Constants.CHARSET));
		while (br.readLine() != null) {
		    lines++;
		}
	    } catch (Exception e) {
		String filename = countfile == null ? null : countfile
			.getName();
		logger.warn("can not count lines for " + filename + " : "
			+ e.getMessage(), e);
		return lines;
	    } finally {
		if (bis != null) {
		    try {
			bis.close();
		    } catch (IOException e) {

		    }
		}
		if (br != null) {
		    try {
			br.close();
		    } catch (IOException e) {

		    }
		}
	    }
	}

	logger.info("There is " + lines + " to process for "
		+ this.getClass().getSimpleName());
	return lines;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#getNumberOfLinesToProcess()
     */
    public int getNumberOfLinesToProcess() {
	if (this.numberOfLinesToProcess == 0) {
	    // it may not have been calculated yet
	    this.numberOfLinesToProcess = countLines();
	}
	return this.numberOfLinesToProcess;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#getStatus()
     */
    public ImporterStatus getStatus() {
	return this.status;
    }

    /**
     * @return The option
     * @see ImporterConfig#setMaxInsertsBeforeFlush(int)
     */
    protected int getMaxInsertsBeforeFlush() {
	return importerConfig.getMaxInsertsBeforeFlush();
    }

    protected void resetStatusFields() {
	this.currentFile = null;
	this.readFileLine = 0;
	this.totalReadLine = 0;
	this.numberOfLinesToProcess = 0;
	this.status = ImporterStatus.UNPROCESSED;
	this.statusMessage = "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#getErrorMessage()
     */
    public String getStatusMessage() {
	return statusMessage;
    }

}
