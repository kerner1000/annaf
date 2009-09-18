package de.mpg.mpiz.koeln.anna.step.common;

// TODO needed? Maybe use de.kerner.commons.AbstractProgressMonitor
public interface StepProcessObserver {

	void setProgress(int progress, int max);

}
