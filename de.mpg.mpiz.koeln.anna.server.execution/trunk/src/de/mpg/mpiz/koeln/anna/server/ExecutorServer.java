package de.mpg.mpiz.koeln.anna.server;

import de.mpg.mpiz.koeln.anna.step.ExecutableStep;

/**
 * 
 * @lastVisit 2009-09-22
 * @author Alexander Kerner
 *
 */
public interface ExecutorServer extends Server {
	
	void registerStep(ExecutableStep step);

	void unregisterStep(ExecutableStep step);
	
	void shutdown();

}
