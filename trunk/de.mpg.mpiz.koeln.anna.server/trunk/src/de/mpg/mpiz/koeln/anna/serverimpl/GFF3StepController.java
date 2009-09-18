package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.GFF3Server;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.step.Step;

/**
 * 
 * Actually, this class just hands over step (and server) to GFF3StepExecutor
 * @author Alexander Kerner
 * @lastVisit 2009-08-14
 *
 */
class GFF3StepController implements Callable<Void> {

	private final GFF3SepExecutor exe;

	GFF3StepController(Step<GFF3DataBean> step, GFF3Server server, LogDispatcher logger) {
		exe = new GFF3SepExecutor(step, server, logger);
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
