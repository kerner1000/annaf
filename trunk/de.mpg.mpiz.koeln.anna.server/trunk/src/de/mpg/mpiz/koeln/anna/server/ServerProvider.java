package de.mpg.mpiz.koeln.anna.server;

import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.utils.AbstractServiceProvider;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;

public class ServerProvider extends AbstractServiceProvider<Server<GFF3DataBean>> {

	public ServerProvider(BundleContext context) {
		super(context);
	}

	@Override
	protected Class<Server<GFF3DataBean>> getServiceClass() {
		return null;
	}

}
