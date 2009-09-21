package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.ArrayList;
import java.util.Collection;

import de.mpg.koeln.anna.core.events.AnnaEvent;
import de.mpg.mpiz.koeln.anna.server.AnnaEventListener;

class EventHandler {

	private final Collection<AnnaEventListener> observers = new ArrayList<AnnaEventListener>();

	synchronized void broadcastEvent(AnnaEvent event) {
		if (observers.isEmpty())
			return;
		for (AnnaEventListener l : observers) {
			l.eventOccoured(event);
		}
	}

	synchronized void addEventListener(AnnaEventListener observer) {
		observers.add(observer);
	}

}
