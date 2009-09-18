package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.kerner.osgi.commons.utils.AbstractServiceProvider;
import de.mpg.mpiz.koeln.anna.server.Server;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;

public class ServerActivator implements BundleActivator {

	private LogDispatcher logger = null;
	private AbstractServiceProvider<DataProxy> provider = null;

	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		provider = new DataProxyProvider(context);
		logger = new LogDispatcherImpl(context);
		final Server service = new ServerImpl(provider, logger);
		context.registerService(Server.class.getName(), service,
				new Hashtable());
		logger.debug(this, "activated");
	}

	public void stop(BundleContext context) throws Exception {
		// TODO method stub
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
