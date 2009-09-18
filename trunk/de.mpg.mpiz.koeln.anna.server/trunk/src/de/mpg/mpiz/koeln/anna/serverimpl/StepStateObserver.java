package de.mpg.mpiz.koeln.anna.serverimpl;

import de.mpg.mpiz.koeln.anna.step.Step;

public interface StepStateObserver<V> {
	
	void stepRegistered(Step<V> step);

	void stepStarted(Step<V> step);

	void stepFinished(Step<V> step, boolean success);

	void stepChecksNeedToRun(Step<V> step);

	void stepWaitForReq(Step<V> step);
}
