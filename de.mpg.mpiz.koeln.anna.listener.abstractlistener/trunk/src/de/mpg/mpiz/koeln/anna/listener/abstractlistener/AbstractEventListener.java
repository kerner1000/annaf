package de.mpg.mpiz.koeln.anna.listener.abstractlistener;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.kerner.commons.osgi.utils.ServiceRetriever;
import de.kerner.commons.osgi.utils.ServiceRetrieverImpl;
import de.mpg.mpiz.koeln.anna.core.events.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.server.AnnaServer;

public abstract class AbstractEventListener implements BundleActivator, AnnaEventListener {

	private volatile ServiceRetriever<AnnaServer> retriever;
	
	public void start(BundleContext context) throws Exception {
		this.retriever = new ServiceRetrieverImpl<AnnaServer>(context, AnnaServer.class);
		AnnaServer s = retriever.getService();
		s.addEventListener(this);
	}

	public void stop(BundleContext context) throws Exception {
		retriever = null;
	}

}
