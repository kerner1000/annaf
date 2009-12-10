package de.mpg.mpiz.koeln.anna.server.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.mpg.mpiz.koeln.anna.step.AnnaStep;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-11-12
 *
 */
public abstract class StepSheduler implements Callable<Void> {
	
//	private final static Log logger = new Log(StepSheduler.class);
	private final ExecutorService pool = Executors.newSingleThreadExecutor();
	protected final AnnaStep step;
	protected final EventHandler handler;
	protected volatile AnnaSepExecutor exe;

	StepSheduler(AnnaStep step, EventHandler handler) {
		this.step = step;
		this.handler = handler;
		this.exe = new AnnaSepExecutor(step, handler);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":"
				+ exe.getClass().getSimpleName();
	}
	
	public synchronized void start(){
		pool.submit(exe);
		pool.shutdown();
	}
}
