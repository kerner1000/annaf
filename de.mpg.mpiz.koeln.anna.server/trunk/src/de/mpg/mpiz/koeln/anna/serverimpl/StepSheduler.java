package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-22
 *
 */
public abstract class StepSheduler implements Callable<Void> {
	
	protected final AnnaStep step;
	protected final EventHandler handler;
	protected final LogDispatcher logger;
	protected AnnaSepExecutor exe;

	StepSheduler(AnnaStep step, EventHandler handler, LogDispatcher logger) {
		this.step = step;
		this.handler = handler;
		this.logger = logger;
		this.exe = new AnnaSepExecutor(step, handler, logger);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":"
				+ exe.getClass().getSimpleName();
	}

}
