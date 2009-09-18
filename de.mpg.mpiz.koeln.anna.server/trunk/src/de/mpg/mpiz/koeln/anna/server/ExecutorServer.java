package de.mpg.mpiz.koeln.anna.server;

import de.mpg.mpiz.koeln.anna.step.Step;

public interface ExecutorServer<V> extends Server<V> {
	
	void registerStep(Step<V> step);

	void unregisterStep(Step<V> step);

}
