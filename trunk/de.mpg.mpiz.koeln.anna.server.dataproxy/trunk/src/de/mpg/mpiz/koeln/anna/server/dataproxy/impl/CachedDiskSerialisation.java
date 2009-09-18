package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import java.io.File;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.impl.GFF3DataBeanImpl;

class CachedDiskSerialisation extends AbstractDiskSerialisation {
	
	CachedDiskSerialisation() {
		super();
	}
	
	CachedDiskSerialisation(LogDispatcher logger) {
		super(logger);
	}

	private volatile boolean dirty = true;
	private volatile DataBean data = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized <V extends DataBean> V readDataBean(File file, Class<V> v)
			throws DataBeanAccessException {
		if(dirty || data == null)
			data = super.readDataBean(file, v);
			return (V) data;
	}
	
	public synchronized <V extends DataBean> void writeDataBean(V v, File file) throws DataBeanAccessException {
		this.dirty = true;
		super.writeDataBean(v, file);
	}

	@SuppressWarnings("unchecked")
	public <V extends DataBean> V getNewDataBean() {
		// TODO: WHY CAST ?!?
		return (V) new GFF3DataBeanImpl();
	}

}
