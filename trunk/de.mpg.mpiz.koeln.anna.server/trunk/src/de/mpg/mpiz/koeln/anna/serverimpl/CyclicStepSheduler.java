package de.mpg.mpiz.koeln.anna.serverimpl;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.koeln.anna.core.events.AnnaEvent;
import de.mpg.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.server.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-22
 * @thradSave custom
 *
 */
public class CyclicStepSheduler extends StepSheduler implements
		AnnaEventListener {

	CyclicStepSheduler(AnnaStep step, EventHandler handler, LogDispatcher logger) {
		super(step, handler, logger);
		handler.addEventListener(this);
	}

	public Void call() throws Exception {
		synchronized (exe) {
			while (true) {
				logger.debug(this,"running " + step);
				this.exe = new AnnaSepExecutor(step, handler, logger);
				exe.call();
				logger.debug(this,"done, waiting");
				exe.wait();
				logger.debug(this,"awake again");
			}
		}
	}

	public void eventOccoured(AnnaEvent event) {
		if (event instanceof StepStateChangeEvent) {
			logger.debug(this,"received step state changed event");
			StepStateChangeEvent c = (StepStateChangeEvent) event;
			if (c.getStep().equals(step)) {
				logger.debug(this,"it was us, ignoring");
			} else {
				synchronized (exe) {
					logger.debug(this,"it was someone else, trying to wake up (if asleep)");
					exe.notifyAll();
				}
			}
		}
	}
}
