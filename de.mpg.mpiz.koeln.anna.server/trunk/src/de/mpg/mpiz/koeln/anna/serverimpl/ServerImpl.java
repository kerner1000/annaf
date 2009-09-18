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
import de.kerner.osgi.commons.utils.AbstractServiceProvider;
import de.mpg.mpiz.koeln.anna.server.Server;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.step.Step;

/**
 * 
 * @ThreadSave (individual guarded)
 * 
 */
public class ServerImpl implements Server<GFF3DataBean> {

	// TODO path
	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"configuration"
					+ File.separatorChar + "server.properties");
	private final Properties properties;
	private final ExecutorService exe = Executors.newCachedThreadPool();
	private final StepStateObserver<GFF3DataBean> observer;
	private final AbstractServiceProvider<GFF3DataBean> dataProxyProvder;
	private final LogDispatcher logger;

	ServerImpl(final AbstractServiceProvider<GFF3DataBean> provider,
			final LogDispatcher logger) {
		if (logger != null)
			this.logger = logger;
		else
			this.logger = new ConsoleLogger();
		this.observer = new StepStateObserverImpl(this.logger);
		this.dataProxyProvder = provider;
		properties = getPropertes();
		logger.debug(this, "loaded properties: " + properties);
	}

	ServerImpl(AbstractServiceProvider<GFF3DataBean> provider) {
		this.logger = new ConsoleLogger();
		this.observer = new StepStateObserverImpl(logger);
		this.dataProxyProvder = provider;
		properties = getPropertes();
		logger.debug(this, "loaded properties: " + properties);
	}

	public void registerStep(Step<GFF3DataBean> step) {
		observer.stepRegistered(step);
		StepController controller = new StepController(step, this, logger);
		synchronized (exe) {
			exe.submit(controller);
		}
		logger.debug(this, "registered step " + step);
	}

	public void unregisterStep(Step<GFF3DataBean> step) {
		System.err.println(this + ": unregistering step " + step);
		// TODO method stub

	}

	// observer is final
	public StepStateObserver<GFF3DataBean> getStepStateObserver() {
		return observer;
	}

	// properties is final
	public Properties getServerProperties() {
		return new Properties(properties);
	}

	// dataProxyProvider is final
	public AbstractServiceProvider<?> getDataProxyProvider() {
		return dataProxyProvder;
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
