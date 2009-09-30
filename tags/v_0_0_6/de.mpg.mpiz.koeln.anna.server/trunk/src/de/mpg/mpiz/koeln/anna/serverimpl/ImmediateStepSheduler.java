package de.mpg.mpiz.koeln.anna.serverimpl;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-22
 * @thradSave custom
 * 
 */
class ImmediateStepSheduler extends StepSheduler {

	ImmediateStepSheduler(AnnaStep step, EventHandler handler,
			LogDispatcher logger) {
		super(step, handler, logger);
	}

	public Void call() throws Exception {
		start();
		return null;
	}
}
