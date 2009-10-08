package de.mpg.mpiz.koeln.anna.abstractstep;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.commons.StringUtils;
import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.kerner.osgi.commons.utils.ServiceNotAvailabeException;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.server.AnnaServer;

public abstract class AbstractAnnaStep<V> implements BundleActivator, AnnaStep {

	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"configuration" + File.separatorChar + "step.properties");
	private volatile ServiceTracker tracker;
	private volatile Properties properties;
	protected volatile LogDispatcher logger = new ConsoleLogger();
	private State state = State.LOOSE;

	// fields volatile
	protected void init(BundleContext context) throws StepExecutionException {
		this.logger = new LogDispatcherImpl(context);
		tracker = new ServiceTracker(context, AnnaServer.class.getName(), null);
		tracker.open();
		try {
			properties = getPropertes();
		} catch (Exception e) {
			logger.error(this, StringUtils.getString(
					"could not load settings from ", PROPERTIES_FILE
							.getAbsolutePath(), ", using defaults"));
		}
	}
	
	public void start(BundleContext context) throws Exception {
		logger.debug(this, "starting step " + this);
		init(context);
		try{
		getAnnaServer().registerStep(this);
		}catch(Exception e){
			final AbstractAnnaStep<?> as = new DummyStep(this.toString());
			as.setState(AbstractAnnaStep.State.ERROR);
			getAnnaServer().registerStep(as);
		}
		
	}

	public void stop(BundleContext context) throws Exception {
		logger.debug(this, "stopping service");
		tracker.close();
		tracker = null;
	}
	
	public AnnaServer getAnnaServer() throws ServiceNotAvailabeException{
		AnnaServer server = (AnnaServer) tracker.getService();
		if(server == null)
			throw new ServiceNotAvailabeException();
		return server;
	}

	public abstract DataProxy<V> getDataProxy() throws ServiceNotAvailabeException;

	public abstract boolean requirementsSatisfied(DataProxy<V> proxy)
			throws StepExecutionException;

	public abstract boolean canBeSkipped(DataProxy<V> proxy)
			throws StepExecutionException;

	public abstract boolean run(DataProxy<V> proxy)
			throws StepExecutionException;

	public boolean requirementsSatisfied() throws StepExecutionException {
		DataProxy<V> proxy;
		try {
			proxy = getDataProxy();
		} catch (ServiceNotAvailabeException e) {
			logger.error(this, e.getLocalizedMessage(), e);
			throw new StepExecutionException(this, e);
		}
		return requirementsSatisfied(proxy);
	}

	public boolean canBeSkipped() throws StepExecutionException {
		DataProxy<V> proxy;
		try {
			proxy = getDataProxy();
		} catch (ServiceNotAvailabeException e) {
			logger.error(this, e.getLocalizedMessage(), e);
			throw new StepExecutionException(this, e);
		}
		return canBeSkipped(proxy);
	}

	public boolean run() throws StepExecutionException {
		DataProxy<V> proxy;
		try {
			proxy = getDataProxy();
		} catch (ServiceNotAvailabeException e) {
			logger.error(this, e.getLocalizedMessage(), e);
			throw new StepExecutionException(this, e);
		}
		return run(proxy);
	}

	public synchronized Properties getStepProperties() {
		return properties;
	}

	private Properties getPropertes() throws IOException {
		final Properties defaultProperties = initDefaults();
		final Properties pro = new Properties(defaultProperties);
		FileInputStream fi = null;
		try {
			logger.info(this, StringUtils.getString("loading settings from ",
					PROPERTIES_FILE));
			fi = new FileInputStream(PROPERTIES_FILE);
			pro.load(fi);
		} finally {
			if (fi != null)
				fi.close();
		}
		return pro;
	}

	private Properties initDefaults() {
		final Properties pro = new Properties();
		return pro;
	}

	public final synchronized State getState() {
		return state;
	}

	public final synchronized void setState(State state) {
		this.state = state;
	}
}
