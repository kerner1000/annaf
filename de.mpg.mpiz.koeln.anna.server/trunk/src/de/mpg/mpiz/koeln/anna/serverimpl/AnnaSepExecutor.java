package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.Server;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-22
 * @thradSave custom
 * 
 */
class AnnaSepExecutor implements Callable<Void> {

	public final static Object LOCK = Server.class;
	private final AnnaStep step;
	private final LogDispatcher logger;
	private final EventHandler handler;

	AnnaSepExecutor(AnnaStep step, EventHandler handler, LogDispatcher logger) {
		this.step = step;
		this.logger = logger;
		this.handler = handler;
	}

	public Void call() throws Exception {
		boolean success = true;
		stepStateChanged(State.CHECK_NEED_TO_RUN);
		final boolean b = step.canBeSkipped();
		if (b) {
			logger.info(this, "step " + step
					+ " does not need to run, skipping");
			stepStateChanged(State.SKIPPED);
			stepFinished(success);
			return null;
		}
		logger.debug(this, "step " + step + " needs to run");
		stepStateChanged(State.WAIT_FOR_REQ);
		synchronized (LOCK) {
			while (!step.requirementsSatisfied()) {
				logger.debug(this, "requirements for step " + step
						+ " not satisfied, putting it to sleep");
				LOCK.wait();
			}
			logger.debug(this, "requirements for step " + step + " satisfied");
		}
		try {
			success = runStep();
		} catch (Exception e) {
			if (e instanceof StepExecutionException) {
				logger.warn(this, e.getLocalizedMessage(), e);
			} else {
				logger.error(this, e.getLocalizedMessage(), e);
			}
			stepStateChanged(State.ERROR);
			success = false;
		}
		stepFinished(success);
		return null;
	}

	private boolean runStep() throws StepExecutionException {
		logger.debug(this, "step " + step + " running");
		stepStateChanged(State.RUNNING);
		return step.run();
	}

	private void stepStateChanged(State state) {
		step.setState(state);
		handler.stepStateChanged(step);
	}

	private void stepFinished(boolean success) {
		logger.debug(this, "step " + step + " done running");
		if (success && !(step.getState().equals(ObservableStep.State.SKIPPED))) {
			stepStateChanged(ObservableStep.State.DONE);
		} else if (!success
				&& !(step.getState().equals(ObservableStep.State.SKIPPED))) {
			stepStateChanged(ObservableStep.State.ERROR);
		}
	}

	public String toString() {
		return this.getClass().getSimpleName() + ":"
				+ step.getClass().getSimpleName();
	}
}
