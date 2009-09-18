package de.mpg.mpiz.koeln.anna.server.data.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import de.bioutils.gff.element.NewGFFElement;
import de.bioutils.fasta.FASTAElement;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;

@SuppressWarnings("unchecked")
public class DataBeanImpl implements DataBean {

	private static final long serialVersionUID = 2776955959983685805L;
	private ArrayList<FASTAElement> verifiedGenesFastas = new ArrayList<FASTAElement>();
	private ArrayList<FASTAElement> inputSequences = new ArrayList<FASTAElement>();
	private ArrayList<NewGFFElement> verifiedGenesGFFs = new ArrayList<NewGFFElement>();
	private ArrayList<NewGFFElement> predictedGenesGFFs = new ArrayList<NewGFFElement>();
	private ArrayList<NewGFFElement> repeatMaskerGFF = new ArrayList<NewGFFElement>();
	private File conradTrainingFile = null;
//	private final LogDispatcher logger;

	public DataBeanImpl(LogDispatcher logger) {
//		if (logger == null)
//			this.logger = new ConsoleLogger();
//		else
//			this.logger = logger;
	}

	public DataBeanImpl() {
//		this.logger = new ConsoleLogger();
	}

	private <V> V deepCopy(Class<V> c, Serializable s) throws IOException,
			ClassNotFoundException {
		if (c == null || s == null)
			throw new NullPointerException();
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		new ObjectOutputStream(bs).writeObject(s);
		ByteArrayInputStream bi = new ByteArrayInputStream(bs.toByteArray());
		V v = c.cast(new ObjectInputStream(bi).readObject());
		bs.close();
		bi.close();
		return v;
	}

	public synchronized void setInputSequences(
			ArrayList<? extends FASTAElement> sequences)
			throws DataBeanAccessException {
		if (sequences == null)
			throw new NullPointerException();
		if (sequences.size() == 0) {
//			logger.warn(this, "sequences are empty");
			return;
		}
		try {
			this.inputSequences.clear();
			this.inputSequences.addAll(deepCopy(ArrayList.class, sequences));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}
	
	public synchronized ArrayList<? extends FASTAElement> getInputSequences()
			throws DataBeanAccessException {
		try {
			return new ArrayList<FASTAElement>(deepCopy(ArrayList.class,
					inputSequences));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized void setVerifiedGenesFasta(
			ArrayList<? extends FASTAElement> sequences)
			throws DataBeanAccessException {
		if (sequences == null)
			throw new NullPointerException();
		if (sequences.size() == 0) {
//			logger.warn(this, "sequences are empty");
			return;
		}
		try {
			this.verifiedGenesFastas.clear();
			this.verifiedGenesFastas
					.addAll(deepCopy(ArrayList.class, sequences));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized void setVerifiedGenesGff(
			ArrayList<? extends NewGFFElement> el) throws DataBeanAccessException {
		if (el == null)
			throw new NullPointerException();
		if (el.size() == 0) {
//			logger.warn(this, "sequences are empty");
			return;
		}
		try {
			this.verifiedGenesGFFs.clear();
			this.verifiedGenesGFFs.addAll(deepCopy(ArrayList.class, el));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized ArrayList<? extends FASTAElement> getVerifiedGenesFasta()
			throws DataBeanAccessException {
		try {
			return new ArrayList<FASTAElement>(deepCopy(ArrayList.class,
					verifiedGenesFastas));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized ArrayList<? extends NewGFFElement> getVerifiedGenesGff()
			throws DataBeanAccessException {
		try {
			return new ArrayList<NewGFFElement>(deepCopy(ArrayList.class,
					verifiedGenesGFFs));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized File getConradTrainingFile() {
		if (conradTrainingFile == null) {
			return null;
		}
		return new File(conradTrainingFile.getAbsolutePath());
	}

	public synchronized void setConradTrainingFile(File file)
			throws DataBeanAccessException {
		if (file == null || !file.exists() || !file.canRead())
			throw new DataBeanAccessException("conrad training file invalid ("
					+ file + ")");
		this.conradTrainingFile = new File(file.getAbsolutePath());
	}

	public synchronized ArrayList<? extends NewGFFElement> getPredictedGenesGff()
			throws DataBeanAccessException {
		try {
			return new ArrayList<NewGFFElement>(deepCopy(ArrayList.class,
					predictedGenesGFFs));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized void setPredictedGenesGtf(
			ArrayList<? extends NewGFFElement> elements)
			throws DataBeanAccessException {
		if (elements == null)
			throw new NullPointerException();
		if (elements.size() == 0) {
//			logger.warn(this, "sequences are empty");
			return;
		}
		try {
			this.predictedGenesGFFs.clear();
			this.predictedGenesGFFs.addAll(deepCopy(ArrayList.class, elements));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized ArrayList<? extends NewGFFElement> getRepeatMaskerGff()
			throws DataBeanAccessException {
		try {
			return new ArrayList<NewGFFElement>(deepCopy(ArrayList.class,
					repeatMaskerGFF));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}
	}

	public synchronized void setRepeatMaskerGff(
			ArrayList<? extends NewGFFElement> elements)
			throws DataBeanAccessException {
		if (elements == null)
			throw new NullPointerException();
		if (elements.size() == 0) {
//			logger.warn(this, "sequences are empty");
			return;
		}
		try {
			this.repeatMaskerGFF.clear();
			this.repeatMaskerGFF.addAll(deepCopy(ArrayList.class, elements));
		} catch (IOException e) {
			throw new DataBeanAccessException(e);
		} catch (ClassNotFoundException e) {
			throw new DataBeanAccessException(e);
		}

	}

	public String toString() {
		return this.getClass().getSimpleName()
				+ Integer.toHexString(this.hashCode());
	}
}
