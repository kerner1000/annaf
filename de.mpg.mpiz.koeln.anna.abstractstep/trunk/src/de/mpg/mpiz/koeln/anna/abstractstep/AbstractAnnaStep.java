package de.mpg.mpiz.koeln.anna.abstractstep;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.commons.StringUtils;
import de.kerner.commons.file.FileUtils;
import de.kerner.commons.logging.Log;
import de.kerner.commons.osgi.utils.ServiceNotAvailabeException;
import de.mpg.mpiz.koeln.anna.server.AnnaServer;
import de.mpg.mpiz.koeln.anna.server.data.DataProxy;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.StepExecutionException;

public abstract class AbstractAnnaStep<V> implements BundleActivator, AnnaStep {

	private final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"configuration" + File.separatorChar + "step.properties");
	private volatile ServiceTracker tracker;
	private volatile Properties properties;
	protected volatile Log logger = new Log(AbstractAnnaStep.class);
	private volatile State state = State.LOOSE;

	// fields volatile
	protected synchronized void init(BundleContext context)
			throws StepExecutionException {
		tracker = new ServiceTracker(context, AnnaServer.class.getName(), null);
		tracker.open();
		try {
			properties = getPropertes();
		} catch (Exception e) {
			logger.error(StringUtils.getString("could not load settings from ",
					PROPERTIES_FILE.getAbsolutePath(), ", using defaults"));
		}
	}

	public void start(BundleContext context) throws Exception {
		//logger.debug("starting step " + this);
		try {
			init(context);
			getAnnaServer().registerStep(this);
		} catch (Throwable e) {
			synchronized (this) {
				logger.debug("an exception orroured while initiation or registration of step, creating dummy step", e);
				final AbstractAnnaStep<?> as = new DummyStep(this.toString());
				as.setState(State.ERROR);
				getAnnaServer().registerStep(as);
			}
		}
	}

	public void stop(BundleContext context) throws Exception {
		logger.debug("stopping service");
		tracker.close();
		tracker = null;
	}

	public AnnaServer getAnnaServer() throws ServiceNotAvailabeException {
		AnnaServer server = (AnnaServer) tracker.getService();
		if (server == null)
			throw new ServiceNotAvailabeException();
		return server;
	}

	public abstract DataProxy<V> getDataProxy()
			throws ServiceNotAvailabeException;

	public abstract boolean requirementsSatisfied(DataProxy<V> proxy)
			throws Throwable;

	public abstract boolean canBeSkipped(DataProxy<V> proxy) throws Throwable;

	public abstract boolean run(DataProxy<V> proxy) throws Throwable;

	public boolean requirementsSatisfied() throws StepExecutionException {
		try {
			final DataProxy<V> proxy = getDataProxy();
			return requirementsSatisfied(proxy);
		} catch (Throwable t) {
			throw new StepExecutionException(this, t);
		}
	}

	public boolean canBeSkipped() throws StepExecutionException {
		try {
			final DataProxy<V> proxy = getDataProxy();
			return canBeSkipped(proxy);
		} catch (Throwable t) {
			throw new StepExecutionException(this, t);
		}
	}

	public boolean run() throws StepExecutionException {
		try {
			final DataProxy<V> proxy = getDataProxy();
			return run(proxy);
		} catch (Throwable t) {
			throw new StepExecutionException(this, t);
		}
	}

	public synchronized Properties getStepProperties() {
		return properties;
	}

	private Properties getPropertes() throws IOException {
		final Properties defaultProperties = initDefaults();
		final Properties pro = new Properties(defaultProperties);
		FileInputStream fi = null;
		try {
			logger.debug(StringUtils.getString("loading settings from ",
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

	public final State getState() {
		return state;
	}

	public final void setState(State state) {
		this.state = state;
	}

	public List<String> requirementsNeeded(DataProxy<V> proxy) throws Throwable {
		return Collections.emptyList();
	}

	public List<String> requirementsNeeded() {
		try {
			final DataProxy<V> proxy = getDataProxy();
			return requirementsNeeded(proxy);
		} catch (Throwable t) {
			logger.error(t.getLocalizedMessage(), t);
			return new ArrayList<String>();
		}
	}
}
