package de.mpg.mpiz.koeln.anna.serverimpl;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.koeln.anna.core.events.AnnaEvent;
import de.mpg.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.server.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-22
 * @thradSave custom
 * 
 */
public class CyclicStepSheduler extends StepSheduler implements
		AnnaEventListener {

	CyclicStepSheduler(AnnaStep step, EventHandler handler, LogDispatcher logger) {
		super(step, handler, logger);
		handler.addEventListener(this);
	}

	public Void call() throws Exception {
		while (true) {
			synchronized (this) {
				// maybe check state of cyclic step before executing it again?
				logger.debug(this, "running " + step);
				this.exe = new AnnaSepExecutor(step, handler, logger);
				exe.call();
				logger.debug(this, "done, waiting");
				this.wait();
				logger.debug(this, "awake again");
			}
		}
	}

	public void eventOccoured(AnnaEvent event) {
		if (event instanceof StepStateChangeEvent) {
			logger.debug(this, "received step state changed event");
			StepStateChangeEvent c = (StepStateChangeEvent) event;
			if (c.getStep().equals(step)) {
				logger.debug(this, "it was us, ignoring");
			} else if (!c.getStep().getState()
					.equals(ObservableStep.State.DONE)) {
				logger.debug(this, "step state change was not to state "
						+ ObservableStep.State.DONE + ", ignoring");
			} else {
				synchronized (this) {
					logger
							.debug(this,
									"it was someone else, trying to wake up (if asleep)");
					this.notifyAll();
				}
			}
		}
	}
}
