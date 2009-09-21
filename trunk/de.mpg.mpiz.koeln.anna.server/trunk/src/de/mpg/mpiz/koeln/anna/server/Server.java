package de.mpg.mpiz.koeln.anna.server;

import java.util.Properties;

/**
 * 
 * @lastVisit 2009-09-18
 * @author Alexander Kerner
 *
 */
public interface Server {

	Properties getServerProperties();
	
	void addEventListener(AnnaEventListener observer);

}
