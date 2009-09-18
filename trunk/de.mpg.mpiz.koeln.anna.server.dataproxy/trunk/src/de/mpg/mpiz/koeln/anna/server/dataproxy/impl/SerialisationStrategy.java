package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import java.io.File;

import de.mpg.mpiz.koeln.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;


interface SerialisationStrategy {

	DataBean readFromDisk(File file) throws DataBeanAccessException;

	void writeDataBean(DataBean dataBean, File file)
			throws DataBeanAccessException;

}
