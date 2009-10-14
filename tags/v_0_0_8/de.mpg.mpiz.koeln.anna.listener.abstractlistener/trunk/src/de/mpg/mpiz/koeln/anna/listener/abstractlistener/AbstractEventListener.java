package de.mpg.mpiz.koeln.anna.listener.abstractlistener;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.kerner.osgi.commons.utils.ServiceRetriever;
import de.kerner.osgi.commons.utils.ServiceRetrieverImpl;
import de.mpg.mpiz.koeln.anna.server.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.server.AnnaServer;

public abstract class AbstractEventListener implements BundleActivator, AnnaEventListener {

	private volatile ServiceRetriever<AnnaServer> retriever;
	protected LogDispatcher logger;
	
	public void start(BundleContext context) throws Exception {
		this.retriever = new ServiceRetrieverImpl<AnnaServer>(context, AnnaServer.class);
		AnnaServer s = retriever.getService();
		s.addEventListener(this);
		logger = new LogDispatcherImpl(context);
	}

	public void stop(BundleContext context) throws Exception {
		retriever = null;
		logger = null;
	}

}
