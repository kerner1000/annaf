package de.mpg.mpiz.koeln.anna.server.impl;


import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.core.events.AnnaEvent;
import de.mpg.mpiz.koeln.anna.core.events.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep;

public class CyclicStepSheduler extends ImmediateStepSheduler implements
		AnnaEventListener {
	
	private final static Log logger = new Log(CyclicStepSheduler.class);

	CyclicStepSheduler(AnnaStep step, EventHandler handler) {
		super(step, handler);
		handler.addEventListener(this);
	}

	@Override
	public Void call() throws Exception {
		// do nothing until some other steps are done
		return null;
	}

	public void eventOccoured(AnnaEvent event) {
		if (event instanceof StepStateChangeEvent) {
			logger.debug("received step state changed event " + event);
			final StepStateChangeEvent c = (StepStateChangeEvent) event;
			if (c.getStep().equals(step)) {
				logger.debug("it was us, ignoring");
			} else if (!(c.getStep().getState().equals(
					ObservableStep.State.DONE) || c.getStep().getState()
					.equals(ObservableStep.State.SKIPPED))) {
				logger.debug("step state change was not to state "
						+ ObservableStep.State.DONE + "/"
						+ ObservableStep.State.SKIPPED + ", ignoring");
			} else {
				logger.debug("someone else finished, starting");
				exe = new AnnaSepExecutor(step, handler);
				start();
			}
		}
	}
}
