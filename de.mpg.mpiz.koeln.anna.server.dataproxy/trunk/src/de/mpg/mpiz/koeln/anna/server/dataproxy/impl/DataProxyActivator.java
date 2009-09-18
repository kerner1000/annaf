package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;

public class DataProxyActivator implements BundleActivator {

	private LogDispatcher logger = null;

	public void start(BundleContext context) throws Exception {
		logger = new LogDispatcherImpl(context);
		DataProxy proxy = new DataProxyImpl(new SerializationStrategySer(logger), logger);
		context.registerService(DataProxy.class.getName(), proxy,
				new Hashtable<Object, Object>());
	}

	public void stop(BundleContext context) throws Exception {
		// TODO method stub
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}	
}
