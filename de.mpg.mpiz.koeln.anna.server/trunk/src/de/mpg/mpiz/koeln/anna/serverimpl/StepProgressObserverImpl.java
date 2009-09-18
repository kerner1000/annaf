package de.mpg.mpiz.koeln.anna.serverimpl;

import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;

/**
 * 
 * @Threadsave
 * 
 */
class StepProgressObserverImpl implements StepProcessObserver {

	private volatile int progress = -1;
	private volatile int max = 100;

	public void setProgress(int progress, int max) {
		this.progress = progress;
		this.max = max;
		printProgress();
	}

	private double getProgress() {
		if (max == 100 && progress > -1 && progress <= 100)
			return (double) progress / (double) max;
		return progress;
	}

	private void printProgress() {
		System.out.printf("%s: %3.2f", this, getProgress());
		System.out.println();
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
