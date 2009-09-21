package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import java.io.File;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.impl.GFF3DataBeanImpl;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-21
 *
 */
class CachedDiskSerialisation extends GFF3DiskSerialisation {
	
	CachedDiskSerialisation() {
		super();
	}
	
	CachedDiskSerialisation(LogDispatcher logger) {
		super(logger);
	}

	private volatile boolean dirty = false;
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized <V extends DataBean> V readDataBean(File file, Class<V> v)
			throws DataBeanAccessException {
		if(dirty){
			logger.debug(this, "data dirty, reading from disk");
			data = super.readDataBean(file, v);} else {
				logger.debug(this, "reading data from cache");
			}
			return (V) data;
	}
	
	public synchronized <V extends DataBean> void writeDataBean(V v, File file) throws DataBeanAccessException {
		this.dirty = true;
		logger.debug(this, "writing data");
		super.writeDataBean(v, file);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
