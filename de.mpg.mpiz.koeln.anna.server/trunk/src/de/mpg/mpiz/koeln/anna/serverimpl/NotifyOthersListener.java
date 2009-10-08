package de.mpg.mpiz.koeln.anna.serverimpl;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.koeln.anna.core.events.AnnaEvent;
import de.mpg.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.server.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

/**
 * <p> Helper class, to release lock when step finished.</p>
 * @author Alexander Kerner
 *
 */
class NotifyOthersListener implements AnnaEventListener {
	
	private final LogDispatcher logger;
	
	NotifyOthersListener(LogDispatcher logger) {
		this.logger = logger;
	}

	public void eventOccoured(AnnaEvent event) {
		if(event instanceof StepStateChangeEvent){
			final State state = ((StepStateChangeEvent) event).getState();
			// state "SKIPPED" not needed, because skipped steps never acquire lock
			if(state.equals(State.DONE) || state.equals(State.ERROR)){
				synchronized (AnnaSepExecutor.LOCK) {
					logger.debug(this, "notifying others");
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
