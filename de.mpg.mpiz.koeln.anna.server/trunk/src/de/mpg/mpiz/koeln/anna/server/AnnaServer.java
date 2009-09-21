package de.mpg.mpiz.koeln.anna.server;


public interface AnnaServer extends ExecutorServer {
	
	final static String PROPERTIES_KEY_PREFIX = "anna.server.";
	final static String WORKING_DIR_KEY = PROPERTIES_KEY_PREFIX
			+ "workingdir";	

}
