package de.mpg.mpiz.koeln.anna.abstractstep;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.kerner.commons.osgi.utils.ServiceNotAvailabeException;
import de.mpg.mpiz.koeln.anna.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataProxy;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataProxy;
import de.mpg.mpiz.koeln.anna.step.StepExecutionException;

// TODO: class implementation is duplicate to "AbstractGFF3AnnaStep". That is bad!
public abstract class AbstractGFF3WrapperStep extends AbstractWrapperStep<GFF3DataBean>{
	
	private volatile ServiceTracker tracker;

	// tracker volatile
	@Override
	protected void init(BundleContext context) throws StepExecutionException {
		super.init(context);
		tracker = new ServiceTracker(context, GFF3DataProxy.class.getName(),
				null);
		tracker.open();
	}

	// tracker volatile
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
