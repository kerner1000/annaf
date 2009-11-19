package de.mpg.mpiz.koeln.anna.server.data.impl;

import java.io.File;

import de.kerner.commons.file.FileUtils;
import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.data.DataBean;
import de.mpg.mpiz.koeln.anna.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.data.impl.GFF3DataBeanImpl;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-21
 * @threadSave no need to synchronize (data is volatile), methods from "super"
 *             already are threadsave.
 * 
 */
class CachedDiskSerialisation extends GFF3DiskSerialisation {
	
	private final Log logger = new Log(CachedDiskSerialisation.class);

	private volatile boolean dirty = true;

	@SuppressWarnings("unchecked")
	@Override
	public <V extends DataBean> V readDataBean(File file, Class<V> v)
			throws DataBeanAccessException {
		if (FileUtils.fileCheck(file, true))
			;
		if (dirty) {
			logger.debug("data dirty, reading from disk");
			data = super.readDataBean(file, v);
			dirty = false;
		} else {
			logger.debug("reading data from cache");
		}
		return (V) data;
	}

	public <V extends DataBean> void writeDataBean(V v, File file)
			throws DataBeanAccessException {
		try {
			super.writeDataBean(v, file);
		} finally {
			this.dirty = true;
		}
	}
	
	protected <V extends DataBean> V handleCorruptData(File file, Throwable t) {
		dirty = true;
		data = new GFF3DataBeanImpl();
		return super.handleCorruptData(file, t);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
