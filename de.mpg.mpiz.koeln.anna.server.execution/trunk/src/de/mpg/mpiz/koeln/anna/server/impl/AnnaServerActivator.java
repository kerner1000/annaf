package de.mpg.mpiz.koeln.anna.server.impl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.server.AnnaServer;

/**
 * @lastVisit 2009-11-12
 * @author Alexander Kerner
 *
 */
public class AnnaServerActivator implements BundleActivator {

	private final static Log logger = new Log(AnnaServerActivator.class);

	public void start(BundleContext context) throws Exception {
		final AnnaServer service = new AnnaServerImpl();
		context.registerService(AnnaServer.class.getName(), service,
				new Hashtable<Object, Object>());
		logger.debug("activated");
	}

	public void stop(BundleContext context) throws Exception {
		logger.debug("stopping (TODO implement)");
		// TODO implement
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
