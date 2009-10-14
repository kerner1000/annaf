package de.mpg.mpiz.koeln.anna.listener.annafinishedlistener;

import java.util.Collection;

import de.mpg.koeln.anna.core.events.AnnaEvent;
import de.mpg.mpiz.koeln.anna.listener.abstractlistener.AbstractEventListener;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

public class FinishedListener extends AbstractEventListener {
	
	private final static String PRE_LINE =  "!!!!!!!!!!!!!!!!!!!!!!";
	private final static String POST_LINE = PRE_LINE;
	
	private volatile boolean error = false;
	
	public void eventOccoured(AnnaEvent event) {
		if(areWeDone(event)){
			logger.info(this, PRE_LINE);
			if(error){
				logger.info(this, "pipeline finished (with errors)!");
			} else {
				logger.info(this, "pipeline finished!");
			}
			logger.info(this, POST_LINE);
		} else {
			// ignore
		}
	}
	
	private boolean areWeDone(AnnaEvent event){
		final Collection<AnnaStep> eventList = event.getRegisteredSteps();
		for(AnnaStep s : eventList){
			if(!(s.getState().isFinished())){
				return false;
			}
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
