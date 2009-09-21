package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.anna.server.data.impl.GFF3DataBeanImpl;

public class GFF3DiskSerialisation extends AbstractDiskSerialisation {
	
	protected volatile DataBean data = new GFF3DataBeanImpl();
	
	GFF3DiskSerialisation() {
		super();
	}

	GFF3DiskSerialisation(LogDispatcher logger) {
		super(logger);
	}

	@SuppressWarnings("unchecked")
	public <V extends DataBean> V getNewDataBean() {
		// TODO: WHY CAST ?!?
		return (V) new GFF3DataBeanImpl();
	}

}
