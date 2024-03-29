package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-21
 *
 */
abstract class AbstractDiskSerialisation implements SerialisationStrategy {

	protected final LogDispatcher logger;
	
	AbstractDiskSerialisation() {
		this.logger = new ConsoleLogger();
	}

	AbstractDiskSerialisation(LogDispatcher logger) {
		this.logger = logger;
	}

	protected void objectToFile(Serializable s, File file) throws IOException {
		if (s == null || file == null)
			throw new NullPointerException(s + " + " + file
					+ " must not be null");
		OutputStream fos = null;
		ObjectOutputStream outStream = null;
		try {
			fos = new FileOutputStream(file);
			outStream = new ObjectOutputStream(fos);
			outStream.writeObject(s);
		} finally {
			if (outStream != null)
				outStream.close();
			if (fos != null)
				fos.close();
		}
	}

	protected <V> V fileToObject(Class<V> c, File file) throws IOException,
			ClassNotFoundException {
		if (c == null || file == null)
			throw new NullPointerException(c + " + " + file
					+ " must not be null");
		InputStream fis = null;
		ObjectInputStream inStream = null;
		try {
			fis = new FileInputStream(file);
			inStream = new ObjectInputStream(fis);
			V v = c.cast(inStream.readObject());
			return v;
		} finally {
			if (inStream != null)
				inStream.close();
			if (fis != null)
				fis.close();
		}
	}

	protected <V extends DataBean> V handleCorruptData(File file, Throwable t) {
		logger.warn(this, file.toString() + " corrupt, returning new one");
		if (file.delete()) {
			logger.info(this, "deleted corrupted data");
		} else {
			logger.warn(this, "could not delete corrupt data " + file);
		}
		return getNewDataBean();
	}

	public synchronized <V extends DataBean> V readDataBean(File file,
			Class<V> v) throws DataBeanAccessException {
		try {
			final V data = fileToObject(v, file);
			logger.debug(this, "reading data from file");
			return data;
		} catch (EOFException e) {
			logger.warn(this, e.getLocalizedMessage(), e);
			return handleCorruptData(file, e);
		} catch (StreamCorruptedException e) {
			logger.warn(this, e.getLocalizedMessage(), e);
			return handleCorruptData(file, e);
		} catch (Throwable t) {
			logger.error(this, t.getLocalizedMessage(), t);
			throw new DataBeanAccessException(t);
		}
	}

	public synchronized <V extends DataBean> void writeDataBean(V v, File file)
			throws DataBeanAccessException {
		try {
			logger.debug(this, "writing data to file");
			objectToFile(v, file);
		} catch (IOException e) {
			logger.error(this, e.toString(), e);
			throw new DataBeanAccessException(e);
		}
	}
}
