package de.mpg.mpiz.koeln.anna.server;

import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.utils.AbstractServiceProvider;

class GFF3ServerProvider extends AbstractServiceProvider<GFF3Server>{

	GFF3ServerProvider(BundleContext context) {
		super(context);
	}

	@Override
	protected Class<GFF3Server> getServiceClass() {
		return GFF3Server.class;
	}

}
