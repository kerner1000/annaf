package de.mpg.mpiz.koeln.anna.server;

import java.util.Properties;

import de.mpg.mpiz.koeln.anna.serverimpl.StepStateObserver;

/**
 * 
 * @lastVisit 2009-09-18
 * @author Alexander Kerner
 *
 */
public interface Server {

	Properties getServerProperties();

	StepStateObserver getStepStateObserver();

}
