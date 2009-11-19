package de.mpg.mpiz.koeln.anna.server.impl;

import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.core.events.AnnaEvent;
import de.mpg.mpiz.koeln.anna.core.events.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

/**
 * <p> Helper class, to release lock when step finished.</p>
 * @author Alexander Kerner
 *
 */
class NotifyOthersListener implements AnnaEventListener {
	
	private final static Log logger = new Log(NotifyOthersListener.class);
	
	NotifyOthersListener() {
		
	}

	public void eventOccoured(AnnaEvent event) {
		if(event instanceof StepStateChangeEvent){
			final State state = ((StepStateChangeEvent) event).getState();
			// state "SKIPPED" not needed, because skipped steps never acquire lock
			if(state.equals(State.DONE) || state.equals(State.ERROR)){
				synchronized (AnnaSepExecutor.LOCK) {
					logger.debug("notifying others");
					AnnaSepExecutor.LOCK.notifyAll();
				}
			} else {
				// nothing
			}
		}else {
			// nothing
		}
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
