package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.concurrent.Callable;

import de.kerner.osgi.commons.logger.dispatcher.ConsoleLogger;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.Server;
import de.mpg.mpiz.koeln.anna.step.Step;

/**
 * 
 * @lastVisit 2009-08-14
 * @author Alexander Kerner
 *
 */
abstract class AbstractStepExecutor implements Callable<Boolean> {

	protected final Step step;
	protected final Server server;
	protected final LogDispatcher logger;

	AbstractStepExecutor(Step step, Server server) {
		this.step = step;
		this.server = server;
		this.logger = new ConsoleLogger();
	}

	AbstractStepExecutor(Step step, Server server, LogDispatcher logger) {
		this.step = step;
		this.server = server;
		if (logger != null)
			this.logger = logger;
		else
			this.logger = new ConsoleLogger();
	}
}
