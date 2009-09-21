package de.mpg.mpiz.koeln.anna.server;

import java.util.EventListener;
import de.mpg.koeln.anna.core.events.AnnaEvent;

public interface AnnaEventListener extends EventListener {
	
	void eventOccoured(AnnaEvent event);

}
