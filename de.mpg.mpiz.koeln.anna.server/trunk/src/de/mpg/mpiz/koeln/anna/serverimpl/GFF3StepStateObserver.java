package de.mpg.mpiz.koeln.anna.serverimpl;

import de.mpg.mpiz.koeln.anna.step.Step;

/**
 * @lastVisit 2009-09-18
 * @author Alexander Kerner
 *
 */
public interface GFF3StepStateObserver {
	
	void stepRegistered(Step<?> step);

	void stepStarted(Step<?> step);

	void stepFinished(Step<?> step, boolean success);

	void stepChecksNeedToRun(Step<?> step);

	void stepWaitForReq(Step<?> step);
}
