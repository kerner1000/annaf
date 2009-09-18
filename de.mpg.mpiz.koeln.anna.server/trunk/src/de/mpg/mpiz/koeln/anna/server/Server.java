package de.mpg.mpiz.koeln.anna.server;

import java.util.Properties;

import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.serverimpl.GFF3StepStateObserver;

/**
 * 
 * @lastVisit 2009-09-18
 * @author Alexander Kerner
 *
 */
public interface Server<V> {

	final static String PROPERTIES_KEY_PREFIX = "anna.server.";
	final static String WORKING_DIR_KEY = PROPERTIES_KEY_PREFIX
			+ "workingdir";	

	Properties getServerProperties();

	GFF3StepStateObserver getStepStateObserver();
	
	DataProxy<V> getDataProxy();

}
