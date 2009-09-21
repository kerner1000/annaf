package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.server.Server;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

class AnnaSepExecutor implements Callable<Boolean> {

	private final AnnaStep step;
	private final LogDispatcher logger;
	private final EventHandler handler;

	AnnaSepExecutor(AnnaStep step, EventHandler handler, LogDispatcher logger) {
		this.step = step;
		this.logger = logger;
		this.handler = handler;
	}

	public Boolean call() throws Exception {
		do {
			boolean success = true;
			try {
				handler.broadcastEvent(new StepStateChangeEvent(this, step,
						ObservableStep.State.CHECK_NEED_TO_RUN));
				final boolean b = step.canBeSkipped();
				if (b) {
					logger.info(this, "step " + step
							+ " does not need to run, skipping");
					step.setSkipped(true);
					handler.broadcastEvent(new StepStateChangeEvent(this, step,
							ObservableStep.State.SKIPPED));
					return success;
				}
				logger.debug(this, "step " + step + " needs to run");
				handler.broadcastEvent(new StepStateChangeEvent(this, step,
						ObservableStep.State.WAIT_FOR_REQ));
				synchronized (Server.class) {
					try {
						while (!step.requirementsSatisfied()) {
							logger.debug(this, "requirements for step " + step
									+ " not satisfied, putting it to sleep");
							Server.class.wait();
						}
						logger.debug(this, "requirements for step " + step
								+ " satisfied");
					} catch (InterruptedException e) {
						logger.debug(this, "notifying others");
						Server.class.notifyAll();
						StepUtils.handleException(this, e);
					}
					logger.debug(this, "notifying others");
					Server.class.notifyAll();
				}
				success = runStep();
				stepFinished(success);

				// catch other Exceptions?
			} catch (StepExecutionException e) {
				logger.info(this, "executing step " + step + " was erroneous",
						e);
				step.setState(AnnaStep.State.ERROR);
			}
			return success;
		} while (step.isCyclic());
	}

	private boolean runStep() throws StepExecutionException {
		logger.debug(this, "step " + step + "running");
		handler.broadcastEvent(new StepStateChangeEvent(this, step,
				ObservableStep.State.RUNNING));
		return step.run();
	}

	private void stepFinished(boolean success) {
		logger.debug(this, "step " + step + "done running");
		if(success){
			handler.broadcastEvent(new StepStateChangeEvent(this, step,
					ObservableStep.State.DONE));
		} else {
			handler.broadcastEvent(new StepStateChangeEvent(this, step,
					ObservableStep.State.ERROR));
		}
		synchronized (Server.class) {
			logger.debug(this, "notifying others");
			Server.class.notifyAll();
		}
	}

	public String toString() {
		return this.getClass().getSimpleName() + ":"
				+ step.getClass().getSimpleName();
	}
}
