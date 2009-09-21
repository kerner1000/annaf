package de.mpg.mpiz.koeln.anna.server.dataproxy.impl;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;


public class SimpleDiskSerialisation extends GFF3DiskSerialisation {

	public SimpleDiskSerialisation() {
		super();
	}
	
	public SimpleDiskSerialisation(LogDispatcher logger) {
		super(logger);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
