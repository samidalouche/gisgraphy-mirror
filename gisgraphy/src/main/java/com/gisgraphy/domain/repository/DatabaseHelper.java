/**
 * 
 */
package com.gisgraphy.domain.repository;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernatespatial.postgis.PostgisDialectNG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.gisgraphy.domain.valueobject.Constants;
import com.sun.corba.se.pept.transport.Connection;

/**
 * Default implementation of {@link IDatabaseHelper}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Component
public class DatabaseHelper extends HibernateDaoSupport implements IDatabaseHelper {

	protected static final Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);

	/* (non-Javadoc)
	 * @see com.gisgraphy.domain.repository.IDatabaseHelper#execute(java.io.File, boolean)
	 */
	public boolean execute(final File file, final boolean continueOnError) throws Exception {
		if (file == null) {
			throw new IllegalArgumentException("Can not execute a null file");
		}

		if (!file.exists()) {
			throw new IllegalArgumentException("The specified file does not exists and can not be executed : " + file.getAbsolutePath());
		}
		logger.info("will execute sql file " + file.getAbsolutePath());

		return (Boolean) this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws PersistenceException {

				BufferedReader reader;
				
				
				InputStream inInternal = null;
				// uses a BufferedInputStream for better performance
				try {
				    inInternal = new BufferedInputStream(new FileInputStream(file));
				} catch (FileNotFoundException e) {
				    throw new RuntimeException(e);
				}
				try {
				    reader = new BufferedReader(new InputStreamReader(inInternal,
					    Constants.CHARSET));
				} catch (UnsupportedEncodingException e) {
				    throw new RuntimeException(e);
				}
				String line;
				int count = 0;
				boolean errorDuringProcess = false;
				try {
					while ((line = reader.readLine()) != null) {
						line = line.trim();
						 // comment or empty or psql command
						if (line.startsWith("--") || line.length() == 0 || line.startsWith("\\"))
						{
							continue;
						} 
						Query createIndexQuery = session.createSQLQuery(line);
						try {
						    int nbupdate = createIndexQuery.executeUpdate();
						    logger.info("execution of line : "+line+" modify "+nbupdate+" lines");
						} catch (Exception e) {
						    	errorDuringProcess = true;
							logger.error("Error on line "+count+" ("+line +") :" +e);
							if (!continueOnError){
							    throw new PersistenceException(e);
							}
						} 
					}
				} catch (IOException e) {
					logger.error("error on line "+count+" : "+e);
				} 
				return !errorDuringProcess;
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.gisgraphy.domain.repository.IDatabaseHelper#generateSqlCreationSchemaFile(java.io.File)
	 */
	public void generateSqlCreationSchemaFile(File outputFileName){
	   createSqlSchemaFile(outputFileName,true,false,false);
	}
	
	/* (non-Javadoc)
	 * @see com.gisgraphy.domain.repository.IDatabaseHelper#generateSqlDropSchemaFile(java.io.File)
	 */
	public void generateSqlDropSchemaFile(File outputFileName){
	    createSqlSchemaFile(outputFileName,false,true,false);
	}
	
	/* (non-Javadoc)
	 * @see com.gisgraphy.domain.repository.IDatabaseHelper#DropAllTables()
	 */
	public List<SQLException> DropAllTables(){
	    return createSqlSchemaFile(null,false,true,true);
	}
	
	/* (non-Javadoc)
	 * @see com.gisgraphy.domain.repository.IDatabaseHelper#createAllTables()
	 */
	public List<SQLException> createAllTables(){
	    return createSqlSchemaFile(null,true,false,true);
	}
	
	/* (non-Javadoc)
	 * @see com.gisgraphy.domain.repository.IDatabaseHelper#dropAndRecreateAllTables()
	 */
	public List<SQLException> dropAndRecreateAllTables(){
	    return createSqlSchemaFile(null,true,true,true);
	}
	
	
	

	private List<SQLException> createSqlSchemaFile(File outputFileName,boolean create, boolean drop, boolean execute ){
	Assert.notNull(outputFileName,"Can not create a sql schema in a null file, please specify a valid one");
	AnnotationConfiguration config = new AnnotationConfiguration();
	config.setProperty("hibernate.dialect",PostgisDialectNG.class.getName());
		config.configure();
		SchemaExport schema =null;
		if (execute == true){
		java.sql.Connection connection = getSession().connection();
		schema = new SchemaExport(config,connection);
		} else {
		   schema = new SchemaExport(config);
		}
		if (outputFileName != null){
		    schema.setOutputFile(outputFileName.getAbsolutePath());
		}
		logger.info("will create the Database schema");
		if (create == true){
		    schema.create(true, true);
		}else if (drop == true){
		    schema.drop(true, true);
		}
		schema.execute(true, execute, drop, create);
		return schema.getExceptions();
	}
	
}
