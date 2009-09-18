package de.mpg.mpiz.koeln.anna.server;

import java.util.Properties;

import de.kerner.osgi.commons.utils.AbstractServiceProvider;
import de.mpg.mpiz.koeln.anna.serverimpl.StepStateObserver;
import de.mpg.mpiz.koeln.anna.step.Step;

public interface Server<V> {

	final static String PROPERTIES_KEY_PREFIX = "anna.server.";
	final static String WORKING_DIR_KEY = PROPERTIES_KEY_PREFIX
			+ "workingdir";

	void registerStep(Step<V> step);

	void unregisterStep(Step<V> step);
	
	
	// Move to other type ??

	Properties getServerProperties();

	StepStateObserver<V> getStepStateObserver();
	
	AbstractServiceProvider<?> getDataProxyProvider();

}
