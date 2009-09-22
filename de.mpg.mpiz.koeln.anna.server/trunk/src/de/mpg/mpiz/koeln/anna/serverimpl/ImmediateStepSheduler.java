package de.mpg.mpiz.koeln.anna.serverimpl;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;

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
		// call "call()" directly to run in same thread
		synchronized (this) {
			exe.call();
		}
		return null;
	}
}
