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

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.impl.DataBeanImpl;

/**
 * 
 * @ThreadSave
 * 
 */
public class SerializationStrategySer implements SerialisationStrategy {
	
	private final LogDispatcher logger;
	
	public SerializationStrategySer() {
		this.logger = null;
	}
	
	SerializationStrategySer(LogDispatcher logger){
		this.logger = logger;
	}

	public synchronized DataBean readFromDisk(File file)
			throws DataBeanAccessException {
		try {
			final DataBean data = fileToObject(DataBean.class, file);
			logger.debug(this, "reading data from file");
			return data;
		} catch (EOFException e) {
			return handleCorruptData(file, e);
		} catch (StreamCorruptedException e) {
			return handleCorruptData(file, e);
		} catch (IOException e) {
			logger.error(this, e.toString(), e);
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			logger.error(this, e.toString(), e);
			throw new DataBeanAccessException(e);
		}
	}

	private DataBean handleCorruptData(File file, Throwable t) {
		logger.warn(this, file.toString()
				+ " corrupt, returning new one");
		if (file.delete()) {
			logger.info(this, "deleted corrupted dataBean");
		} else {
			logger.warn(this, "could not delete corrupt databean " + file);
		}
		return new DataBeanImpl();
	}

	public synchronized void writeDataBean(DataBean dataBean, File file)
			throws DataBeanAccessException {
		try {
			logger.debug(this, "writing data to file");
			objectToFile(dataBean, file);
		} catch (IOException e) {
			logger.error(this, e.toString(), e);
			throw new DataBeanAccessException(e);
		}
	}

	private void objectToFile(Serializable s, File file)
			throws IOException {
		if (s == null || file == null)
			throw new NullPointerException(s + " + " + file
					+ " must not be null");
		OutputStream fos = new FileOutputStream(file);
		ObjectOutputStream outStream = new ObjectOutputStream(fos);
		outStream.writeObject(s);
		outStream.close();
		fos.close();
	}

	private <V> V fileToObject(Class<V> c, File file)
			throws IOException, ClassNotFoundException {
		if (c == null || file == null)
			throw new NullPointerException(c + " + " + file
					+ " must not be null");
		InputStream fis = new FileInputStream(file);
		ObjectInputStream inStream = new ObjectInputStream(fis);
		V v = c.cast(inStream.readObject());
		inStream.close();
		fis.close();
		return v;
	}

	public String toString() {
		return this.getClass().getSimpleName()
		// +"@"+Integer.toHexString(hashCode())
		;
	}

}
