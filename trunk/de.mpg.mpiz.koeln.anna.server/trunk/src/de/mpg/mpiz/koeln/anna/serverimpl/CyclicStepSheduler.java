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
		// do nothing until some other steps are done
		return null;
	}

	public void eventOccoured(AnnaEvent event) {
		if (event instanceof StepStateChangeEvent) {
			logger.debug(this, "received step state changed event " + event);
			final StepStateChangeEvent c = (StepStateChangeEvent) event;
			if (c.getStep().equals(step)) {
				logger.debug(this, "it was us, ignoring");
			} else if (!(c.getStep().getState().equals(
					ObservableStep.State.DONE) || c.getStep().getState()
					.equals(ObservableStep.State.SKIPPED))) {
				logger.debug(this, "step state change was not to state "
						+ ObservableStep.State.DONE + "/"
						+ ObservableStep.State.SKIPPED + ", ignoring");
			} else {
				logger.debug(this, "someone else finished, starting");
				exe = new AnnaSepExecutor(step, handler, logger);
				start();
			}
		}
	}
}
