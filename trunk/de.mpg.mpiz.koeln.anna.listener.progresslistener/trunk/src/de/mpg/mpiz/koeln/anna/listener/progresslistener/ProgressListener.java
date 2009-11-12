package de.mpg.mpiz.koeln.anna.listener.progresslistener;

import java.util.Collection;
import java.util.Formatter;

import de.kerner.commons.file.FileUtils;
import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.core.events.AnnaEvent;
import de.mpg.mpiz.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.listener.abstractlistener.AbstractEventListener;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

public class ProgressListener extends AbstractEventListener {

	private final static Log logger = new Log(ProgressListener.class);
	private final static String PRE_LINE =  " MONITOR ++++++++++++++++++++++++++++++++++++++++++";
	private final static String POST_LINE = "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++";

	public void eventOccoured(AnnaEvent event) {
		AnnaStep lastChanged = null;
		if (event instanceof StepStateChangeEvent){
			lastChanged = ((StepStateChangeEvent) event).getStep();
		} else {
			logger.info(event);
		}
		printStepStates(event, lastChanged);
	}
	
	private synchronized void printStepStates(AnnaEvent event, AnnaStep lastChangedStep) {
		final Collection<AnnaStep> steps = event.getRegisteredSteps();
		final StringBuilder hans = new StringBuilder();
		hans.append(PRE_LINE);
		hans.append(FileUtils.NEW_LINE);
		for (AnnaStep s :steps) {
			final String s1 = s.toString();
			final String s2 = "state=" + s.getState();	
			final StringBuilder sb = new StringBuilder();
			// TODO better: String.format();
			final Formatter f = new Formatter();
			sb.append(f.format("\t%-28s\t%-22s", s1, s2).toString());
			
			if(s.getState().equals(State.WAIT_FOR_REQ)){
				try {
					sb.append(s.requirementsNeeded());
				} catch (Exception e) {
					logger.error("cannot print needed requirements for step " + s, e);
				}
			}
			
			if (lastChangedStep != null && lastChangedStep.equals(s)) {
				sb.append("\t(changed)");
			}
			hans.append(sb.toString());
			hans.append(FileUtils.NEW_LINE);
		}
		hans.append(POST_LINE);
		logger.info(hans);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
