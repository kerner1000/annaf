package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import java.io.File;

import de.mpg.mpiz.koeln.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;


interface SerialisationStrategy {

	<V extends DataBean> V readDataBean(File file, Class<V> v) throws DataBeanAccessException;

	<V extends DataBean> void writeDataBean(V v, File file)
			throws DataBeanAccessException;
	
	<V extends DataBean> V getNewDataBean();

}
