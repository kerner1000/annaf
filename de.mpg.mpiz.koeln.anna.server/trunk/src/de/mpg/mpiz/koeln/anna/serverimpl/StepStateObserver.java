package de.mpg.mpiz.koeln.anna.serverimpl;

import de.mpg.mpiz.koeln.anna.step.Step;

public interface StepStateObserver {
	
	void stepRegistered(Step step);

	void stepStarted(Step step);

	void stepFinished(Step step, boolean success);

	void stepChecksNeedToRun(Step step);

	void stepWaitForReq(Step step);
}
