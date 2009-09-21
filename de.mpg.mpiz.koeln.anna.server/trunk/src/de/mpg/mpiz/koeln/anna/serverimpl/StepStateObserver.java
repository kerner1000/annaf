package de.mpg.mpiz.koeln.anna.serverimpl;

import de.mpg.mpiz.koeln.anna.step.ObservableStep;

/**
 * @lastVisit 2009-09-18
 * @author Alexander Kerner
 *
 */
public interface StepStateObserver {
	
	void stepRegistered(ObservableStep step);

	void stepStarted(ObservableStep step);

	void stepFinished(ObservableStep step, boolean success);

	void stepChecksNeedToRun(ObservableStep step);

	void stepWaitForReq(ObservableStep step);
}
