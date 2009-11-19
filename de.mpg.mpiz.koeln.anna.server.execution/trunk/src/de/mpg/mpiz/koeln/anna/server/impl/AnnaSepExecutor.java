package de.mpg.mpiz.koeln.anna.server.impl;

import java.util.concurrent.Callable;

import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.server.Server;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep;
import de.mpg.mpiz.koeln.anna.step.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-11-12
 * @thradSave custom
 * 
 */
class AnnaSepExecutor implements Callable<Void> {

	private final static Log log = new Log(AnnaSepExecutor.class);
	public final static Object LOCK = Server.class;
	private final AnnaStep step;
	private final EventHandler handler;

	AnnaSepExecutor(AnnaStep step, EventHandler handler) {
		this.step = step;
		this.handler = handler;
	}

	public Void call() throws Exception {
		boolean success = true;
		stepStateChanged(State.CHECK_NEED_TO_RUN);
		final boolean b = step.canBeSkipped();
		if (b) {
			log.info("step " + step
					+ " does not need to run, skipping");
			stepStateChanged(State.SKIPPED);
			stepFinished(success);
			return null;
		}
		log.debug("step " + step + " needs to run");
		stepStateChanged(State.WAIT_FOR_REQ);
		synchronized (LOCK) {
			while (!step.requirementsSatisfied()) {
				log.debug("requirements for step " + step
						+ " not satisfied, putting it to sleep");
				LOCK.wait();
			}
			log.debug("requirements for step " + step + " satisfied");
		}
		try {
			success = runStep();
		} catch (Exception e) {
			if (e instanceof StepExecutionException) {
				log.warn(e.getLocalizedMessage(), e);
			} else {
				log.error(e.getLocalizedMessage(), e);
			}
			stepStateChanged(State.ERROR);
			success = false;
		}
		stepFinished(success);
		return null;
	}

	private boolean runStep() throws StepExecutionException {
		log.debug("step " + step + " running");
		stepStateChanged(State.RUNNING);
		return step.run();
	}

	private void stepStateChanged(State state) {
		step.setState(state);
		handler.stepStateChanged(step);
	}

	private void stepFinished(boolean success) {
		log.debug("step " + step + " done running");
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
