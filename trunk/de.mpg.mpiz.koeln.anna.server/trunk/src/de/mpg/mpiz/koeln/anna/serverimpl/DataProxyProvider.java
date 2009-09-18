package de.mpg.mpiz.koeln.anna.serverimpl;

import org.osgi.framework.BundleContext;

import de.kerner.osgi.commons.utils.AbstractServiceProvider;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-08-14
 *
 */
class DataProxyProvider<V> extends AbstractServiceProvider<DataProxy<GFF3DataBean>>{

	DataProxyProvider(BundleContext context) {
		super(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<DataProxy<GFF3DataBean>> getServiceClass() {
		return (Class<DataProxy<GFF3DataBean>>) this.getClass();
	}
}
