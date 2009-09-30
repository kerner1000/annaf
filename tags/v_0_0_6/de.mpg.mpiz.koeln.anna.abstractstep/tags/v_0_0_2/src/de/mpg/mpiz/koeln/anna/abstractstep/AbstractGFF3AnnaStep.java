package de.mpg.mpiz.koeln.anna.abstractstep;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.osgi.commons.utils.ServiceNotAvailabeException;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.server.dataproxy.GFF3DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;

public abstract class AbstractGFF3AnnaStep extends
		AbstractAnnaStep<GFF3DataBean> {

	private volatile ServiceTracker tracker;

	@Override
	protected void init(BundleContext context) throws StepExecutionException {
		super.init(context);
		tracker = new ServiceTracker(context, GFF3DataProxy.class.getName(),
				null);
		tracker.open();
	}

	@Override
	public DataProxy<GFF3DataBean> getDataProxy()
			throws ServiceNotAvailabeException {
		GFF3DataProxy proxy = (GFF3DataProxy) tracker.getService();
		if (proxy == null)
			throw new ServiceNotAvailabeException();
		return proxy;
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.tracker.close();
		tracker = null;
		super.finalize();
	}
}
