package de.mpg.mpiz.koeln.anna.serverimpl;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.Server;
import de.mpg.mpiz.koeln.anna.step.AbstractStep;
import de.mpg.mpiz.koeln.anna.step.Step;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

class LocalSepExecutor extends AbstractStepExecutor {

	LocalSepExecutor(Step step, Server server, LogDispatcher logger) {
		super(step, server, logger);
	}

	public Boolean call() throws Exception {
		boolean success = true;
		try{
		server.getStepStateObserver().stepChecksNeedToRun(step);
		final boolean b = step.canBeSkipped(server.getDataProxyProvider()
				.getService());
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
				while (!step.requirementsSatisfied(server
						.getDataProxyProvider().getService())) {
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
			step.setState(AbstractStep.State.ERROR);
		}
		return success;
	}
	
	private boolean runStep() throws StepExecutionException {
		final StepProcessObserver listener = new StepProgressObserverImpl();
		logger.debug(this, "step " + step + "running");
		server.getStepStateObserver().stepStarted(step);
		return step
				.run(server.getDataProxyProvider().getService(), listener);
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
