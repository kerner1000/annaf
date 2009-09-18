package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import java.io.File;

import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataModifier;
import de.mpg.mpiz.koeln.anna.server.dataproxy.GFF3DataProxy;

public class GFF3DataProxyImpl implements GFF3DataProxy{
	
	final static String WORKING_DIR_KEY = "anna.server.data.workingDir";
	final static File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR,
			"configuration" + File.separatorChar + "data.properties");
	final static String DATA_FILE_NAME = "data.ser";
	
	private final SerialisationStrategy strategy;
	
	public GFF3DataProxyImpl(final SerialisationStrategy strategy) {
		this.strategy = strategy;
	}
	
	public GFF3DataProxyImpl() {
		this.strategy = new CachedDiskSerialisation();
	}

	private GFF3DataBean getData() throws DataBeanAccessException {
		return strategy.readDataBean(new File(DATA_FILE_NAME), GFF3DataBean.class);
	}
	
	private void setData(GFF3DataBean data) throws DataBeanAccessException {
		strategy.writeDataBean(data, new File(DATA_FILE_NAME));
	}

	public synchronized void modifiyData(DataModifier<GFF3DataBean> v)
			throws DataBeanAccessException {
		final GFF3DataBean data = getData();
		v.modifiyData(data);
		setData(data);
	}

	public GFF3DataBean viewData() throws DataBeanAccessException {
		return getData();
	}
}
