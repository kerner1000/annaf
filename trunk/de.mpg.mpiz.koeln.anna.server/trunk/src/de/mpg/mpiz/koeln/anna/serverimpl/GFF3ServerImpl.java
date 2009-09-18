package de.mpg.mpiz.koeln.anna.serverimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.GFF3Server;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.server.dataproxy.GFF3DataProxy;
import de.mpg.mpiz.koeln.anna.step.GFF3Step;
import de.mpg.mpiz.koeln.anna.step.Step;

public class GFF3ServerImpl implements GFF3Server {
	
	// TODO path
	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"configuration"
					+ File.separatorChar + "server.properties");
	private final Properties properties;
	private final ExecutorService exe = Executors.newCachedThreadPool();
	private final GFF3StepStateObserver observer;
	private final GFF3DataProxy proxy;
	private final LogDispatcher logger;

	GFF3ServerImpl(final  GFF3DataProxy proxy,
			final LogDispatcher logger) {
		if (logger != null)
			this.logger = logger;
		else
			this.logger = new ConsoleLogger();
		this.observer = new StepStateObserverImpl(this.logger);
		this.proxy = proxy;
		properties = getPropertes();
		logger.debug(this, "loaded properties: " + properties);
	}

	GFF3ServerImpl(GFF3DataProxy proxy) {
		this.logger = new ConsoleLogger();
		this.observer = new StepStateObserverImpl(logger);
		this.proxy = proxy;
		properties = getPropertes();
		logger.debug(this, "loaded properties: " + properties);
	}
	
	public void registerStep(Step<GFF3DataBean> step) {
		observer.stepRegistered(step);
		GFF3StepController controller = new GFF3StepController(step, this, logger);
		synchronized (exe) {
			exe.submit(controller);
		}
		logger.debug(this, "registered step " + step);
	}

	public void unregisterStep(Step<GFF3DataBean> step) {
		// TODO Auto-generated method stub
	}

	public void registerStep(GFF3Step step) {
		observer.stepRegistered(step);
		GFF3StepController controller = new GFF3StepController(step, this, logger);
		synchronized (exe) {
			exe.submit(controller);
		}
		logger.debug(this, "registered step " + step);
	}

	// observer is final
	public GFF3StepStateObserver getStepStateObserver() {
		return observer;
	}

	// properties is final
	public Properties getServerProperties() {
		return new Properties(properties);
	}

	public String toString() {
		return this.getClass().getSimpleName();
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
		// pro.setProperty(WORKING_DIR_KEY, WORKING_DIR_VALUE);
		return pro;
	}

	public DataProxy<GFF3DataBean> getDataProxy() {
		return proxy;
	}
}
