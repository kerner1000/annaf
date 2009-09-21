package de.mpg.mpiz.koeln.anna.serverimpl;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.koeln.anna.core.events.AnnaEvent;
import de.mpg.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.server.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;

public class CyclicStepSheduler extends StepSheduler implements
		AnnaEventListener {

	CyclicStepSheduler(AnnaStep step, EventHandler handler, LogDispatcher logger) {
		super(step, handler, logger);
		handler.addEventListener(this);
	}

	public Void call() throws Exception {
		synchronized (this) {
			while (true) {
				System.err.println(this + "running");
				this.exe = new AnnaSepExecutor(step, handler, logger);
				System.err.println(this + "done, waiting");
				this.wait();
				System.err.println(this + "awake again");
			}
		}
	}

	public void eventOccoured(AnnaEvent event) {
		if (event instanceof StepStateChangeEvent) {
			StepStateChangeEvent c = (StepStateChangeEvent) event;
			if (!c.getStep().equals(this)) {
				synchronized (this) {
					this.notifyAll();
				}
			}
		}
	}
}
