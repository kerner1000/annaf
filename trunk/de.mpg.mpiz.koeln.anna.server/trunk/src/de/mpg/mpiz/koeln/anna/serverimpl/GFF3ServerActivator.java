package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.anna.server.GFF3Server;
import de.mpg.mpiz.koeln.anna.server.dataproxy.GFF3DataProxy;

/**
 * @lastVisit 2009-09-18
 * @author Alexander Kerner
 *
 */
public class GFF3ServerActivator implements BundleActivator {

	private LogDispatcher logger = null;
	private GFF3DataProxy proxy = null;

	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		proxy = getProxy(context);
		logger = new LogDispatcherImpl(context);
		final GFF3Server service = new GFF3ServerImpl(proxy, logger);
		context.registerService(GFF3Server.class.getName(), service,
				new Hashtable());
		logger.debug(this, "activated");
	}

	private synchronized GFF3DataProxy getProxy(BundleContext context) {
		final ServiceTracker tracker = new ServiceTracker(context, GFF3DataProxy.class.getName(), null);
		tracker.open();
		return (GFF3DataProxy) tracker.getService();
	}

	public void stop(BundleContext context) throws Exception {
		// TODO method stub
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
