package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.AnnaServer;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;

/**
 * 
 * Actually, this class just hands over step (and server) to GFF3StepExecutor
 * @author Alexander Kerner
 * @lastVisit 2009-08-14
 *
 */
class AnnaStepController implements Callable<Void> {

	private final AnnaSepExecutor exe;

	AnnaStepController(AnnaStep step, AnnaServer server, LogDispatcher logger) {
		exe = new AnnaSepExecutor(step, server, logger);
	}

	public String toString() {
		return this.getClass().getSimpleName() + ":"
				+ exe.getClass().getSimpleName();
	}

	public Void call() throws Exception {
		// call "call()" directly to run in same thread
		exe.call();
		return null;
	}
}
