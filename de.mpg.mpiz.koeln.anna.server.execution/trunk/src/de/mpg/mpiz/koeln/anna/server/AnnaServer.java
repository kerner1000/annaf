package de.mpg.mpiz.koeln.anna.server;

/**
 * 
 * @lastVisit 2009-09-22
 * @author Alexander Kerner
 *
 */
public interface AnnaServer extends ExecutorServer {
	
	final static String PROPERTIES_KEY_PREFIX = "anna.server.";
	final static String WORKING_DIR_KEY = PROPERTIES_KEY_PREFIX
			+ "workingdir";

}
