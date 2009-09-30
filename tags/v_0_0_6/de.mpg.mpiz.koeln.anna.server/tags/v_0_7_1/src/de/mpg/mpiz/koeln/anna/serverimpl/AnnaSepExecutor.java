package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.server.Server;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-22
 * @thradSave custom
 * 
 */
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
		boolean success = true;
		try {
			stepStateChanged(step, State.CHECK_NEED_TO_RUN);
			final boolean b = step.canBeSkipped();
			if (b) {
				logger.info(this, "step " + step
						+ " does not need to run, skipping");
				stepStateChanged(step, State.SKIPPED);
				// success == true;
				return success;
			}
			logger.debug(this, "step " + step + " needs to run");
			stepStateChanged(step, State.WAIT_FOR_REQ);
			synchronized (Server.class) {
				while (!step.requirementsSatisfied()) {
					logger.debug(this, "requirements for step " + step
							+ " not satisfied, putting it to sleep");
					Server.class.wait();
				}
				logger.debug(this, "requirements for step " + step
						+ " satisfied");
				logger.debug(this, "notifying others");
				Server.class.notifyAll();
			}
			success = runStep();
			stepFinished(success);
		} catch (Exception e) {
			logger.info(this, "executing step " + step + " was erroneous", e);
			stepStateChanged(step, State.ERROR);
		}
		return success;
	}

	private synchronized boolean runStep() throws StepExecutionException {
		logger.debug(this, "step " + step + "running");
		stepStateChanged(step, State.RUNNING);
		return step.run();
	}
	
	private synchronized void stepStateChanged(AnnaStep step, State state){
		step.setState(state);
		handler.stepStateChanged(step);
	}

	private void stepFinished(boolean success) {
		logger.debug(this, "step " + step + "done running");
		if (success) {
			stepStateChanged(step, State.DONE);
		} else {
			stepStateChanged(step, State.ERROR);
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
