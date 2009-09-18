package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.gff.element.NewGFFElement;
import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.impl.DataBeanImpl;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;

/**
 * 
 * @ThreadSave (everything protected by this)
 * 
 */
public class DataProxyImpl implements DataProxy {

	private final static String WORKING_DIR_KEY = "anna.server.data.workingDir";
	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"configuration"
					+ File.separatorChar + "data.properties");
	private final static String DATA_FILE_NAME = "data.ser";
	private final Properties properties;
	private final File workingDir;
	private final File file;
	private final SerialisationStrategy strategy;
	private final LogDispatcher logger;
	
	public DataProxyImpl(SerialisationStrategy strategy, LogDispatcher logger) throws FileNotFoundException {
		this.strategy = strategy;
		this.logger = logger;
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
	
	public DataProxyImpl(SerialisationStrategy strategy) throws FileNotFoundException {
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

	public synchronized ArrayList<? extends FASTAElement> getVerifiedGenesFasta()
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getVerifiedGenesFasta();
	}

	public synchronized ArrayList<? extends NewGFFElement> getVerifiedGenesGff()
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getVerifiedGenesGff();
	}

	public synchronized void setVerifiedGenesFasta(
			ArrayList<? extends FASTAElement> sequences)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setVerifiedGenesFasta(sequences);
		strategy.writeDataBean(data, file);
	}

	public synchronized void setVerifiedGenesGff(
			ArrayList<? extends NewGFFElement> elements)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setVerifiedGenesGff(elements);
		strategy.writeDataBean(data, file);
	}

	public synchronized ArrayList<? extends FASTAElement> getInputSequences()
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getInputSequences();
	}

	public synchronized void setInputSequences(
			ArrayList<? extends FASTAElement> fastas)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setInputSequences(fastas);
		strategy.writeDataBean(data, file);
	}

	public synchronized File getConradTrainingFile()
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getConradTrainingFile();
	}

	public synchronized void setConradTrainingFile(File trainingFile)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setConradTrainingFile(trainingFile);
		strategy.writeDataBean(data, file);
	}

	public synchronized ArrayList<? extends NewGFFElement> getPredictedGenesGtf()
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getPredictedGenesGff();
	}

	public synchronized void setPredictedGenesGff(
			ArrayList<? extends NewGFFElement> result)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setPredictedGenesGtf(result);
		strategy.writeDataBean(data, file);
	}

	public synchronized ArrayList<? extends NewGFFElement> getRepeatMaskerGff()
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		return data.getRepeatMaskerGff();
	}

	public synchronized void setRepeatMaskerGff(
			ArrayList<? extends NewGFFElement> elements)
			throws DataBeanAccessException {
		final DataBean data = getDataBean();
		data.setRepeatMaskerGff(elements);
		strategy.writeDataBean(data, file);
	}
	
	public String toString() {
		return this.getClass().getSimpleName()
				+ Integer.toHexString(this.hashCode());
	}

	private DataBean getDataBean() throws DataBeanAccessException {
		if (file.exists() && file.canRead()) {
			return strategy.readFromDisk(file);
		} else {
			logger.debug(this, "cannot read databean " + file + ", creating new databean");
			return new DataBeanImpl();
		}
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
