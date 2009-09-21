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
import de.mpg.mpiz.koeln.anna.server.AnnaServer;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ExecutableStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep;

public class AnnaServerImpl implements AnnaServer {
	
	// TODO path
	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"configuration"
					+ File.separatorChar + "server.properties");
	private final Properties properties;
	private final ExecutorService exe = Executors.newCachedThreadPool();
	private final StepStateObserver observer;
	private final LogDispatcher logger;

	AnnaServerImpl(final LogDispatcher logger) {
		if (logger != null)
			this.logger = logger;
		else
			this.logger = new ConsoleLogger();
		this.observer = new StepStateObserverImpl(this.logger);
		properties = getPropertes();
		logger.debug(this, "loaded properties: " + properties);
	}

	public void unregisterStep(ExecutableStep step) {
		// TODO Auto-generated method stub
	}

	public void registerStep(ExecutableStep step) {
		observer.stepRegistered((ObservableStep) step);
		AnnaStepController controller = new AnnaStepController((AnnaStep) step, this, logger);
		synchronized (exe) {
			exe.submit(controller);
		}
		logger.debug(this, "registered step " + step);
	}

	// observer is final
	public StepStateObserver getStepStateObserver() {
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
}
