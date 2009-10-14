package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.ArrayList;
import java.util.Collection;

import de.mpg.koeln.anna.core.events.AnnaEvent;
import de.mpg.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.server.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;

/**
 * <p> Simple helper class </p>
 * @threadSave all sync to this
 * @author Alexander Kerner
 * @lastVisit 2009-09-22
 *
 */
class EventHandler {

	private final Collection<AnnaEventListener> observers = new ArrayList<AnnaEventListener>();
	private final Collection<AnnaStep> registeredSteps;
	
	public EventHandler(Collection<AnnaStep> registeredSteps) {
		this.registeredSteps = registeredSteps;
	}
	
	public synchronized void stepStateChanged(AnnaStep step){
		broadcastEvent(new StepStateChangeEvent(this, registeredSteps, step));
	}

	private void broadcastEvent(AnnaEvent event) {
		if (observers.isEmpty())
			return;
		for (AnnaEventListener l : observers) {
			l.eventOccoured(event);
		}
	}

	synchronized void addEventListener(AnnaEventListener observer) {
		observers.add(observer);
	}

	synchronized  void removeEventListener(AnnaEventListener observer) {
		observers.remove(observer);
	}

}
