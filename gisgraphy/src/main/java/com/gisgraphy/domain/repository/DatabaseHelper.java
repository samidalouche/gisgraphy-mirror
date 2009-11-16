/**
 * 
 */
package com.gisgraphy.domain.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.persistence.PersistenceException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link IDatabaseHelper}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Component
public class DatabaseHelper extends HibernateDaoSupport implements IDatabaseHelper {

	protected static final Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);

	public void execute(final File file) throws IOException, SQLException {
		if (file == null) {
			throw new IllegalArgumentException("Can not execute a null file");
		}

		if (!file.exists()) {
			throw new IllegalArgumentException("The specified file does not exists and can not be executed : " + file.getAbsolutePath());
		}
		// todo set log4j.xml
		logger.info("Reading from file " + file.getAbsolutePath());

		this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws PersistenceException {

				BufferedReader reader;
				try {
					reader = new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e) {
					throw new PersistenceException(e);
				}
				String line;
				int count = 0;
				try {
					while ((line = reader.readLine()) != null) {
						line = line.trim();
						 // comment or empty or psql command
						if (line.startsWith("--") || line.length() == 0 || line.startsWith("\\"))
						{
							continue;
						} 
							Query createIndexQuery = session.createSQLQuery(line);
							int nbupdate = createIndexQuery.executeUpdate();
							logger.info("execution of line modify "+nbupdate+" lines");
							count++;
					}
				} catch (Exception e) {
					logger.error("error on line "+count+" : "+e);
				} 
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gisgraphy.domain.repository.IDatabaseHelper#CreateGeonamesTables()
	 */
	public void CreateGeonamesTables() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gisgraphy.domain.repository.IDatabaseHelper#DropGeonamesTables()
	 */
	public void DropGeonamesTables() {
		// TODO Auto-generated method stub

	}

}
