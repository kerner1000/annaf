package de.mpg.mpiz.koeln.anna.server.data.impl;

import de.mpg.mpiz.koeln.anna.data.DataBean;
import de.mpg.mpiz.koeln.anna.data.impl.GFF3DataBeanImpl;

public class GFF3DiskSerialisation extends AbstractDiskSerialisation {
	
	protected volatile DataBean data = new GFF3DataBeanImpl();

	@SuppressWarnings("unchecked")
	public <V extends DataBean> V getNewDataBean() {
		return (V) new GFF3DataBeanImpl();
	}

}
