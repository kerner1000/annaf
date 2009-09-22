package de.mpg.mpiz.koeln.anna.server;

import java.util.Properties;

/**
 * 
 * @lastVisit 2009-09-22
 * @author Alexander Kerner
 *
 */
public interface Server {

	Properties getServerProperties();
	
	void addEventListener(AnnaEventListener observer);
	
	void removeEventListener(AnnaEventListener observer);

}
