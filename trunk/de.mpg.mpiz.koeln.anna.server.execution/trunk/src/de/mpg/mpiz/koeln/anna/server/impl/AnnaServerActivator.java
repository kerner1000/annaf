package de.mpg.mpiz.koeln.anna.server.impl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.server.AnnaServer;

/**
 * @lastVisit 2009-11-12
 * @author Alexander Kerner
 *
 */
public class AnnaServerActivator implements BundleActivator {

	private final static Log logger = new Log(AnnaServerActivator.class);
	private volatile BundleContext context;

	public void start(BundleContext context) throws Exception {
		this.context = context;
		final AnnaServer service = new AnnaServerImpl(this);
		context.registerService(AnnaServer.class.getName(), service,
				new Hashtable<Object, Object>());
		logger.debug("server started");
	}

	public void stop(BundleContext context) throws Exception {
		logger.debug("stopping (TODO implement)");
		// TODO implement
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	// TODO: this should be obsolete (services do not need to be unregistered in order to shutdown OSGi)
	void shutdown() {
		logger.debug("shutting down server");
		final ServiceReference reference = context.getServiceReference(AnnaServer.class.getName());
		final boolean b = context.ungetService(reference);
		if(b){
			logger.debug("server down");
		} else {
			logger.debug("server was not running");
		}
	}
}
