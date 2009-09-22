package de.mpg.mpiz.koeln.anna.server;

import java.util.EventListener;
import de.mpg.koeln.anna.core.events.AnnaEvent;

/**
 * 
 * @lastVisit 2009-09-22
 * @author Alexander Kerner
 *
 */
public interface AnnaEventListener extends EventListener {
	
	void eventOccoured(AnnaEvent event);

}
