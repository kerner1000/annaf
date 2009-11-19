package de.mpg.mpiz.koeln.anna.server.impl;

import de.mpg.mpiz.koeln.anna.step.AnnaStep;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-22
 * @thradSave custom
 * 
 */
class ImmediateStepSheduler extends StepSheduler {

	ImmediateStepSheduler(AnnaStep step, EventHandler handler) {
		super(step, handler);
	}

	public Void call() throws Exception {
		start();
		return null;
	}
}
