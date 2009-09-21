package de.mpg.mpiz.koeln.anna.serverimpl;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;

/**
 * 
 * Actually, this class just hands over step (and server) to GFF3StepExecutor
 * @author Alexander Kerner
 * @lastVisit 2009-08-14
 *
 */
class ImmediateStepSheduler extends StepSheduler {

	ImmediateStepSheduler(AnnaStep step, EventHandler handler, LogDispatcher logger) {
		super(step, handler, logger);
	}

	public Void call() throws Exception {
		// call "call()" directly to run in same thread
		exe.call();
		return null;
	}
}
