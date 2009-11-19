package de.mpg.mpiz.koeln.anna.core.events;

import java.util.EventListener;


/**
 * 
 * @lastVisit 2009-09-22
 * @author Alexander Kerner
 *
 */
public interface AnnaEventListener extends EventListener {
	
	void eventOccoured(AnnaEvent event);

}
