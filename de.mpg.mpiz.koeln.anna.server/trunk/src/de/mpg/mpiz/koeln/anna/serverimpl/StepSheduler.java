package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ExecutableStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-22
 *
 */
public abstract class StepSheduler implements Callable<Void> {
	
	private final ExecutorService pool = Executors.newSingleThreadExecutor();
	protected final AnnaStep step;
	protected final EventHandler handler;
	protected final LogDispatcher logger;
	protected volatile AnnaSepExecutor exe;

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
	
	public synchronized void start(){
		try{
		pool.submit(exe);
		} catch (Exception e){
			if(e instanceof StepExecutionException){
				logger.warn(this, e.getLocalizedMessage(), e);
			} else {
				logger.error(this, e.getLocalizedMessage(), e);
			}
			setStepState(step, State.ERROR);
		}
	}
	
	private void setStepState(ExecutableStep step, State state) {
		((AnnaStep) step).setState(state);
		handler.stepStateChanged((AnnaStep) step);
	}

}
