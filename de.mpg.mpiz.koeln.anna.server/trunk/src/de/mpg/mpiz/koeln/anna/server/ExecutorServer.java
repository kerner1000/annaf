package de.mpg.mpiz.koeln.anna.server;

import de.mpg.mpiz.koeln.anna.step.ExecutableStep;

public interface ExecutorServer extends Server {
	
	void registerStep(ExecutableStep step);

	void unregisterStep(ExecutableStep step);

}
