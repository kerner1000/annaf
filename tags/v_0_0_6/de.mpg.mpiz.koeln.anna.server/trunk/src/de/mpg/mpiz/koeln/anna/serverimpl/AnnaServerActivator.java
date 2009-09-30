package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.anna.server.AnnaServer;

/**
 * @lastVisit 2009-09-18
 * @author Alexander Kerner
 *
 */
public class AnnaServerActivator implements BundleActivator {

	private LogDispatcher logger = null;

	public void start(BundleContext context) throws Exception {
		logger = new LogDispatcherImpl(context);
		final AnnaServer service = new AnnaServerImpl(logger);
		context.registerService(AnnaServer.class.getName(), service,
				new Hashtable<Object, Object>());
		logger.debug(this, "activated");
	}

	public void stop(BundleContext context) throws Exception {
		// TODO method stub
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
