package de.mpg.mpiz.koeln.anna.server.data.impl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataProxy;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-11-12
 *
 */
public class GFF3DataProxyActivator implements BundleActivator {

	private final static Log logger = new Log(GFF3DataProxyActivator.class);

	public void start(BundleContext context) throws Exception {
		GFF3DataProxy proxy = new GFF3DataProxyImpl(new CachedDiskSerialisation());
		context.registerService(GFF3DataProxy.class.getName(), proxy,
				new Hashtable<Object, Object>());
	}

	public void stop(BundleContext context) throws Exception {
		logger.debug("stopping. (TODO implement)");
		// TODO implement
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}	
}
