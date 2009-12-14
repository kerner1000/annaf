package de.mpg.mpiz.koeln.anna.listener.annafinishedlistener;

import java.util.Collection;

import de.kerner.commons.logging.Log;
import de.kerner.commons.osgi.utils.ServiceNotAvailabeException;
import de.mpg.mpiz.koeln.anna.core.events.AnnaEvent;
import de.mpg.mpiz.koeln.anna.listener.abstractlistener.AbstractEventListener;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

public class FinishedListener extends AbstractEventListener {
	
	private final static Log logger = new Log(FinishedListener.class);
	
	private final static String PRE_LINE =  "!!!!!!!!!!!!!!!!!!!!!!";
	private final static String POST_LINE = PRE_LINE;
	
	private volatile boolean error = false;
	
	public synchronized void eventOccoured(AnnaEvent event) {
		if(areWeDone(event)){
			logger.info(PRE_LINE);
			if(error){
				logger.info("pipeline finished (with errors)!");
			} else {
				logger.info("pipeline finished!");
			}
			logger.info(POST_LINE);
			shutdownPipeline();
		} else {
			// ignore
		}
	}
	
	private void shutdownPipeline() {
		try {
			super.getServer().shutdown();
		} catch (Throwable e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private boolean areWeDone(AnnaEvent event){
		final Collection<AnnaStep> eventList = event.getRegisteredSteps();
//		boolean done = true;
		for(AnnaStep s : eventList){
			if(!s.getState().isFinished() || s.getState().isActive())
				return false;
			if(s.getState().equals(State.ERROR)){
				this.error = true;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
