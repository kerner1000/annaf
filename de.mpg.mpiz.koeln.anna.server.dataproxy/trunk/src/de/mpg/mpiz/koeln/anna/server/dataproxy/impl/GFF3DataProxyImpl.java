package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataModifier;
import de.mpg.mpiz.koeln.anna.server.dataproxy.GFF3DataProxy;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-21
 *
 */
public class GFF3DataProxyImpl implements GFF3DataProxy{
	
	final static String WORKING_DIR_KEY = "anna.server.data.workingDir";
	final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"configuration" + File.separatorChar + "data.properties");
	final static String DATA_FILE_NAME = "data.ser";
	
	private final SerialisationStrategy strategy;
	private final Properties properties;
	private final File workingDir;
	private final File file;
	private final LogDispatcher logger;
	
	public GFF3DataProxyImpl(final SerialisationStrategy strategy) throws FileNotFoundException {
		this.strategy = strategy;
		this.logger = new ConsoleLogger();
		properties = getPropertes();
		workingDir = new File(properties.getProperty(WORKING_DIR_KEY));
		if(!FileUtils.dirCheck(workingDir, true)){
			final FileNotFoundException e = new FileNotFoundException("cannot access working dir " + workingDir);
			logger.error(this, e, e);
			throw e;
		}
		file = new File(workingDir, DATA_FILE_NAME);
		printProperties();
	}
	
	public GFF3DataProxyImpl() throws FileNotFoundException {
		this.strategy = new CachedDiskSerialisation();
		properties = getPropertes();
		this.logger = new ConsoleLogger();
		workingDir = new File(properties.getProperty(WORKING_DIR_KEY));
		if(!FileUtils.dirCheck(workingDir, true)){
			final FileNotFoundException e = new FileNotFoundException("cannot access working dir " + workingDir);
			logger.error(this, e, e);
			throw e;
		}
		file = new File(workingDir, DATA_FILE_NAME);
		printProperties();
	}

	private GFF3DataBean getData() throws DataBeanAccessException {
		return strategy.readDataBean(file, GFF3DataBean.class);
	}
	
	private void setData(GFF3DataBean data) throws DataBeanAccessException {
		strategy.writeDataBean(data, file);
	}

	public synchronized void modifiyData(DataModifier<GFF3DataBean> v)
			throws DataBeanAccessException {
		final GFF3DataBean data = getData();
		v.modifiyData(data);
		setData(data);
	}

	public GFF3DataBean viewData() throws DataBeanAccessException {
		return getData();
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	private void printProperties() {
		logger.debug(this, " created, properties:");
		logger.debug(this, "\tdatafile=" + file);
	}

	private Properties getPropertes() {
		final Properties defaultProperties = initDefaults();
		final Properties pro = new Properties(defaultProperties);
		try {
			System.out.println(this + ": loading settings from "
					+ PROPERTIES_FILE);
			final FileInputStream fi = new FileInputStream(PROPERTIES_FILE);
			pro.load(fi);
			fi.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(this + ": could not load settings from "
					+ PROPERTIES_FILE.getAbsolutePath() + ", using defaults");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(this + ": could not load settings from "
					+ PROPERTIES_FILE.getAbsolutePath() + ", using defaults");
		}
		return pro;
	}

	private Properties initDefaults() {
		Properties pro = new Properties();
		return pro;
	}
}
