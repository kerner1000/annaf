package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.anna.server.dataproxy.GFF3DataProxy;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-21
 *
 */
public class GFF3DataProxyActivator implements BundleActivator {

	private LogDispatcher logger = null;

	public void start(BundleContext context) throws Exception {
		logger = new LogDispatcherImpl(context);
		GFF3DataProxy proxy = new GFF3DataProxyImpl(new SimpleDiskSerialisation(logger));
		context.registerService(GFF3DataProxy.class.getName(), proxy,
				new Hashtable<Object, Object>());
	}

	public void stop(BundleContext context) throws Exception {
		logger.debug(this, "service stopped!");
		logger = null;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}	
}
