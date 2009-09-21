package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.AnnaServer;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

class AnnaSepExecutor implements Callable<Boolean> {

	private final AnnaServer server;
	private final AnnaStep step;
	private final LogDispatcher logger;
	
	AnnaSepExecutor(AnnaStep step, AnnaServer server, LogDispatcher logger) {
		this.server = server;
		this.step = step;
		this.logger = logger;
	}

	public Boolean call() throws Exception {
		do{
			boolean success = true;
			try{
			server.getStepStateObserver().stepChecksNeedToRun(step);
			final boolean b = step.canBeSkipped();
			if (b) {
				logger.info(this, "step " + step
						+ " does not need to run, skipping");
				step.setSkipped(true);
				server.getStepStateObserver().stepFinished(step, success);
				return success;
			}
			logger.debug(this, "step " + step + " needs to run");
			server.getStepStateObserver().stepWaitForReq(step);
			synchronized (server) {
				try {
					while (!step.requirementsSatisfied()) {
						logger.debug(this, "requirements for step " + step
								+ " not satisfied, putting it to sleep");
						server.wait();
					}
					logger.debug(this, "requirements for step " + step
							+ " satisfied");
				} catch (InterruptedException e) {
					logger.debug(this, "notifying others");
					server.notifyAll();
					StepUtils.handleException(this, e);
				}
				logger.debug(this, "notifying others");
				server.notifyAll();
			}
			success = runStep();
			stepFinished(success);
			
			// catch other Exceptions?
			}catch(StepExecutionException e){
				logger.info(this, "executing step " + step + " was erroneous", e);
				step.setState(AnnaStep.State.ERROR);
			}
			return success;
		} while(step.isCyclic());
	}
	
	private boolean runStep() throws StepExecutionException {
		logger.debug(this, "step " + step + "running");
		server.getStepStateObserver().stepStarted(step);
		return step
				.run();
	}

	private void stepFinished(boolean success){
		logger.debug(this, "step " + step + "done running");
		server.getStepStateObserver().stepFinished(step, success);
		synchronized (server) {
			logger.debug(this, "notifying others");
			server.notifyAll();
		}
	}

	public String toString() {
		return this.getClass().getSimpleName() + ":"
				+ step.getClass().getSimpleName();
	}
}
