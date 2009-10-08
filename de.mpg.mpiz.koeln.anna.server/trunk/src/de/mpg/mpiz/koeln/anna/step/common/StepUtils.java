package de.mpg.mpiz.koeln.anna.step.common;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.ExecutableStep;

/**
 * @lastVisit 2009-08-12
 * @ThreadSave stateless
 * @author Alexander Kerner
 * @Exceptions good
 * 
 */
public class StepUtils {

	private StepUtils() {}

	public static void handleException(ExecutableStep cause, Throwable t,
			LogDispatcher logger) throws StepExecutionException {
		if (logger == null) {
			handleException(cause, t);
		} else {
//			t.printStackTrace();
			logger.error(cause, t.getLocalizedMessage(), t);
			throw new StepExecutionException(cause, t);
		}
	}

	public static void handleException(ExecutableStep cause, Throwable t)
			throws StepExecutionException {
		t.printStackTrace();
		throw new StepExecutionException(cause, t);
	}
}
