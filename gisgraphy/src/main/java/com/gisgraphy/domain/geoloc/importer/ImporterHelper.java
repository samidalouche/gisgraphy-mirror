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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.helper.FeatureClassCodeHelper;

/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *         Useful methods for importer
 */
public class ImporterHelper {

    /**
     * The readme filename (it must not be processed)
     */
    public static final String EXCLUDED_README_FILENAME = "readme.txt";
    /**
     * the all country dump file name
     */
    public static final String ALLCOUTRY_FILENAME = "allCountries.txt";
    /**
     * The regexp that every country file dump matches
     */
    public static final String GEONAMES_COUNTRY_FILE_ACCEPT_REGEX_STRING = "[A-Z][A-Z](.txt)";
    
    public static final String OPENSTREETMAP_US_FILE_ACCEPT_REGEX_STRING = "(US.)[0-9]+(.txt)";
    /**
     * The regexp that every zipped country file dump matches
     */
    public static final String ZIP_FILE_ACCEPT_REGEX_STRING = ".*(.zip)";
    
    public static final String TAR_BZ2_FILE_ACCEPT_REGEX_STRING = ".*(.tar.bz2)";

    protected static final Logger logger = LoggerFactory
	    .getLogger(ImporterHelper.class);

    public static FileFilter countryFileFilter = new FileFilter() {
	public boolean accept(File file) {
	    Pattern patternGeonames = Pattern.compile(GEONAMES_COUNTRY_FILE_ACCEPT_REGEX_STRING);
	    Pattern patternOpenStreetMapUS = Pattern.compile(OPENSTREETMAP_US_FILE_ACCEPT_REGEX_STRING);

	    return (file.isFile() && file.exists())
		    && !EXCLUDED_README_FILENAME.equals(file.getName())
		    && (patternGeonames.matcher(file.getName()).matches() || ALLCOUTRY_FILENAME
			    .equals(file.getName()) || patternOpenStreetMapUS.matcher(file.getName()).matches());
	}
    };

    private static FileFilter ZipFileFilter = new FileFilter() {
	public boolean accept(File file) {
	    Pattern pattern = Pattern.compile(ZIP_FILE_ACCEPT_REGEX_STRING);

	    return (file.isFile() && file.exists())
		    && pattern.matcher(file.getName()).matches();
	}
    };
    
    private static FileFilter tarBZ2FileFilter = new FileFilter() {
	public boolean accept(File file) {
	    Pattern pattern = Pattern.compile(TAR_BZ2_FILE_ACCEPT_REGEX_STRING);

	    return (file.isFile() && file.exists())
		    && pattern.matcher(file.getName()).matches();
	}
    };

    /**
     * @param directoryPath
     *                The directory where Geonames files are
     * @see #GEONAMES_COUNTRY_FILE_ACCEPT_REGEX_STRING
     * @return the allcountries.txt (@see {@linkplain #ALLCOUTRY_FILENAME} file
     *         if present or the list of country file to Import
     */
    public static File[] listCountryFilesToImport(String directoryPath) {

	File dir = new File(directoryPath);

	File[] files = dir.listFiles(countryFileFilter);

	for (File file : files) {
	    if (ALLCOUTRY_FILENAME.equals(file.getName())) {
		files = new File[1];
		files[0] = file;
		logger
			.info(ALLCOUTRY_FILENAME
				+ " is present. Only this file will be imported. all other country files will be ignore");
		break;
	    }
	}

	// for Log purpose
	for (int i = 0; i < files.length; i++) {
	    logger.info(files[i].getName() + " is an importable File");
	}

	return files;
    }

    /**
     * @param directoryPath
     *                The directory where Geonames files are to be downloaded in
     *                order to be processed
     * @see #ZIP_FILE_ACCEPT_REGEX_STRING
     * @return all the zip files present in the specified directory
     */
    public static File[] listZipFiles(String directoryPath) {

	File dir = new File(directoryPath);

	File[] files = dir.listFiles(ZipFileFilter);
	return files;
    }
    
    /**
     * @param directoryPath
     *                The directory where openstreetmap files are to be downloaded in
     *                order to be processed
     * @see #TAR_BZ2_FILE_ACCEPT_REGEX_STRING
     * @return all the zip files present in the specified directory
     */
    public static File[] listTarFiles(String directoryPath) {

	File dir = new File(directoryPath);

	File[] files = dir.listFiles(tarBZ2FileFilter);
	return files;
    }

    /**
     * @param address
     *                the address of the file to be downloaded
     * @param localFileName
     *                the local file name (with absolute path)
     */
    public static void download(String address, String localFileName) {
	logger.info("download file " + address + " to " + localFileName);
	OutputStream out = null;
	URLConnection conn = null;
	InputStream in = null;
	try {
	    URL url = new URL(address);
	    out = new BufferedOutputStream(new FileOutputStream(localFileName));
	    conn = url.openConnection();
	    in = conn.getInputStream();
	    byte[] buffer = new byte[1024];
	    int numRead;
	    long numWritten = 0;
	    while ((numRead = in.read(buffer)) != -1) {
		out.write(buffer, 0, numRead);
		numWritten += numRead;
	    }
	    logger.info(localFileName + "\t" + numWritten);
	}catch (UnknownHostException e) {
	    String errorMessage = "can not download " + address + " to " + localFileName
		    + " : " + e.getMessage()+". if the host exists and is reachable," +
		    		" maybe this links can help : http://www.gisgraphy.com/forum/viewtopic.php?f=3&t=64 ";
	    logger.warn(errorMessage);
	    throw new GeonamesProcessorException(errorMessage,e);
	} catch (Exception e) {
	    logger.warn("can not download " + address + " to " + localFileName
		    + " : " + e.getMessage());
	    throw new GeonamesProcessorException(e);
	} finally {
	    try {
		if (in != null) {
		    in.close();
		}
		if (out != null) {
		    out.flush();
		    out.close();
		}
	    } catch (IOException ioe) {
		logger.error("cannot close streams");
	    }
	}
    }

    /**
     * unzip a file in the same directory as the zipped file
     * 
     * @param file
     *                The file to unzip
     */
    public static void unzipFile(File file) {
	logger.info("will Extracting file: " + file.getName());
	Enumeration<? extends ZipEntry> entries;
	ZipFile zipFile;

	try {
	    zipFile = new ZipFile(file);

	    entries = zipFile.entries();

	    while (entries.hasMoreElements()) {
		ZipEntry entry = (ZipEntry) entries.nextElement();

		if (entry.isDirectory()) {
		    // Assume directories are stored parents first then
		    // children.
		    (new File(entry.getName())).mkdir();
		    continue;
		}

		logger.info("Extracting file: " + entry.getName() + " to "
			+ file.getParent() + File.separator + entry.getName());
		copyInputStream(zipFile.getInputStream(entry),
			new BufferedOutputStream(new FileOutputStream(file
				.getParent()
				+ File.separator + entry.getName())));
	    }

	    zipFile.close();
	} catch (IOException e) {
	    logger.error("can not unzip " + file.getName() + " : "
		    + e.getMessage());
	    throw new GeonamesProcessorException(e);
	}
    }

    private static final void copyInputStream(InputStream in, OutputStream out)
	    throws IOException {
	byte[] buffer = new byte[1024];
	int len;

	while ((len = in.read(buffer)) >= 0) {
	    out.write(buffer, 0, len);
	}

	in.close();
	out.close();
    }

    /**
     * @param fields
     *                the fields corresponding a split line of the csv geonames
     *                file
     * @return the modified fields whith the feature code change to
     *         ADM1,ADM2,ADM3,ADM4 according to the ADMcodes. e.g id adm1code
     *         and Adm2 code are not null : the feature code will be change to
     *         ADM2.
     */
    public static String[] virtualizeADMD(String[] fields) {
	if (fields[7] != null && "ADMD".equals(fields[7]) && fields[6] != null
		&& "A".equals(fields[6])) {
	    // it is an ADMD, will try to detect level
	    int level = Adm.getProcessedLevelFromCodes(fields[10], fields[11],
		    fields[12], fields[13]);
	    if (level != 0) {
		fields[7] = "ADM" + level;
	    }
	}
	return fields;

    }

    /**
     * @param directoryPath
     *                The directory to check. it can be absolute or relative
     * @return true if the path is a directory (not a file) AND exists AND is
     *         writable
     */
    public static boolean isDirectoryAccessible(String directoryPath) {
	File dir = new File(directoryPath);
	return dir.exists() && dir.isDirectory() && dir.canWrite();
    }

    public static String[] correctLastAdmCodeIfPossible(String[] fields) {
	if (FeatureClassCodeHelper.is_Adm(fields[6], fields[7])
		&& !AbstractGeonamesProcessor.isEmptyField(fields, 0, false)) {
	    int level = Adm.getProcessedLevelFromFeatureClassCode(fields[6],
		    fields[7]);
	    switch (level) {
	    case 0:
		return fields;
	    case 1:
		if (AbstractGeonamesProcessor.isEmptyField(fields, 10, false)) {
		    fields[10] = fields[0];// asign adm1code with featureid
		}
		return fields;
	    case 2:
		if (!AbstractGeonamesProcessor.isEmptyField(fields, 10, false)
			&& AbstractGeonamesProcessor.isEmptyField(fields, 11,
				false)) {
		    fields[11] = fields[0];// asign adm2code with featureid
		}
		return fields;
	    case 3:
		if (!AbstractGeonamesProcessor.isEmptyField(fields, 10, false)
			&& !AbstractGeonamesProcessor.isEmptyField(fields, 11,
				false)
			&& AbstractGeonamesProcessor.isEmptyField(fields, 12,
				false)) {
		    fields[12] = fields[0];// asign adm3code with featureid
		}
		return fields;
	    case 4:
		if (!AbstractGeonamesProcessor.isEmptyField(fields, 10, false)
			&& !AbstractGeonamesProcessor.isEmptyField(fields, 11,
				false)
			&& !AbstractGeonamesProcessor.isEmptyField(fields, 12,
				false)
			&& AbstractGeonamesProcessor.isEmptyField(fields, 13,
				false)) {
		    fields[13] = fields[0];// asign adm4code with featureid
		}
		return fields;

	    default:
		return fields;
	    }

	}
	return fields;
    }

    /**
     * @param regexpSemiColumnSeparated
     *                a string with multiple reqgexp separated by ';'
     * @return A list of {@link Pattern} or null if a regexp are not corrects
     */
    public static List<Pattern> compileRegex(String regexpSemiColumnSeparated) {
	try {
	    List<Pattern> patternsList = new ArrayList<Pattern>();
	    String[] acceptRegexpString = regexpSemiColumnSeparated.trim()
		    .split(ImporterConfig.OPTION_SEPARATOR);
	    for (String pattern : acceptRegexpString) {
		if (pattern != null && !pattern.trim().equals("")) {
		    patternsList.add(Pattern.compile(pattern));
		}
	    }
	    logger.info(patternsList.size() + " regex for gisFeatureImporter");
	    return patternsList;
	} catch (RuntimeException e) {
	    return null;
	}
    }

    /**
     * @param secsIn
     *                the number of seconds
     * @return a human reading strings. example :1 hour 6 minuts 40 seconds.
     */
    public static String formatSeconds(long secsIn) {

	long hours = secsIn / 3600,

	remainder = secsIn % 3600, minutes = remainder / 60, seconds = remainder % 60;
	String displayhours = hours == 0 ? "" : hours + " hour"
		+ getPlural(hours);
	String displayMin = minutes == 0 ? "" : minutes + " minut"
		+ getPlural(minutes);
	String displaySec = seconds == 0 ? "" : seconds + " second"
		+ getPlural(seconds);
	return displayhours + displayMin + displaySec;
    }

    private static String getPlural(long count) {
	return count > 1 ? "s " : " ";
    }

}
