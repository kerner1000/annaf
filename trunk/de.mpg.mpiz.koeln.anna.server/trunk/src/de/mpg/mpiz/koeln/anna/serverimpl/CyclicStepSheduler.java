package de.mpg.mpiz.koeln.anna.serverimpl;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.koeln.anna.core.events.AnnaEvent;
import de.mpg.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.server.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep;

public class CyclicStepSheduler extends ImmediateStepSheduler implements
		AnnaEventListener {

	CyclicStepSheduler(AnnaStep step, EventHandler handler, LogDispatcher logger) {
		super(step, handler, logger);
		handler.addEventListener(this);
	}

	@Override
	public Void call() throws Exception {
		synchronized (step) {
			while (step.isCyclic()) {
				// maybe check state of cyclic step before executing it again?
				exe = new AnnaSepExecutor(step, handler, logger);
				super.call();
				logger.debug(this, "done, waiting");
				step.wait();
				logger.debug(this, "awake again");
			}
		}
		return null;
	}

	public void eventOccoured(AnnaEvent event) {
		if (event instanceof StepStateChangeEvent) {
			logger.debug(this, "received step state changed event");
			final StepStateChangeEvent c = (StepStateChangeEvent) event;
			if (c.getStep().equals(step)) {
				logger.debug(this, "it was us, ignoring");
			} else if (!c.getStep().getState()
					.equals(ObservableStep.State.DONE)) {
				logger.debug(this, "step state change was not to state "
						+ ObservableStep.State.DONE + ", ignoring");
			} else {
				synchronized (step) {
					logger
							.debug(this,
									"someone else finished, trying to wake up (if asleep)");
					step.notifyAll();
				}
			}
		}
	}
}
