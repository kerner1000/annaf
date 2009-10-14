package de.mpg.mpiz.koeln.anna.serverimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.server.AnnaServer;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ExecutableStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-22
 * @thradSave custom
 *
 */
public class AnnaServerImpl implements AnnaServer {
	
	// TODO path
	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"configuration"
					+ File.separatorChar + "server.properties");
	private final Collection<AnnaStep> registeredSteps = new HashSet<AnnaStep>();
	private final EventHandler handler;
	private final Properties properties;
	private final ExecutorService exe = Executors.newCachedThreadPool();
	private final LogDispatcher logger;

	AnnaServerImpl(final LogDispatcher logger) {
		if (logger != null)
			this.logger = logger;
		else
			this.logger = new ConsoleLogger();
		properties = getPropertes();
		logger.debug(this, "loaded properties: " + properties);
		handler = new EventHandler(registeredSteps);
		handler.addEventListener(new NotifyOthersListener(logger));
	}

	public synchronized void unregisterStep(ExecutableStep step) {
		// TODO Auto-generated method stub
	}

	public synchronized void registerStep(ExecutableStep step) {
		registeredSteps.add((AnnaStep) step);
		setStepState(step, State.REGISTERED);
		logger.debug(this, "registered step " + step);
		StepSheduler ss;
		if(step.isCyclic()){
			ss = new CyclicStepSheduler((AnnaStep) step, handler, logger);
		} else {
			ss = new ImmediateStepSheduler((AnnaStep) step, handler, logger);
		}
			exe.submit(ss);
	}

	public synchronized Properties getServerProperties() {
		return new Properties(properties);
	}
	
	// handler threadsave
	public void addEventListener(AnnaEventListener observer) {
		handler.addEventListener(observer);
	}
	
	// handler threadsave
	public void removeEventListener(AnnaEventListener observer) {
		handler.removeEventListener(observer);
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	private synchronized Properties getPropertes() {
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
	
	private void setStepState(ExecutableStep step, State state) {
		((AnnaStep) step).setState(state);
		handler.stepStateChanged((AnnaStep) step);
	}
}
