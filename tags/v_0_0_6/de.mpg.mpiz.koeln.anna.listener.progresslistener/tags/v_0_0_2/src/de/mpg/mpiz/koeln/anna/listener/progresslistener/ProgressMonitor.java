package de.mpg.mpiz.koeln.anna.listener.progresslistener;

import java.util.Collection;
import java.util.Formatter;

import de.kerner.commons.file.FileUtils;
import de.mpg.koeln.anna.core.events.AnnaEvent;
import de.mpg.koeln.anna.core.events.StepStateChangeEvent;
import de.mpg.mpiz.koeln.anna.listener.abstractlistener.AbstractEventListener;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;

public class ProgressMonitor extends AbstractEventListener {

	private final static String PRE_LINE =  "+++++++++++++++ MONITOR +++++++++++++++";
	private final static String POST_LINE = "+++++++++++++++++++++++++++++++++++++++";

	public void eventOccoured(AnnaEvent event) {
		AnnaStep lastChanged = null;
		if (event instanceof StepStateChangeEvent){
			lastChanged = ((StepStateChangeEvent) event).getStep();
		} else {
			logger.info(this, event);
		}
		printStepStates(event, lastChanged);
	}
	
	private synchronized void printStepStates(AnnaEvent event, AnnaStep lastChangedStep) {
		final Collection<AnnaStep> steps = event.getRegisteredSteps();
		logger.info(this, PRE_LINE);
		for (AnnaStep s :steps) {
			final String s1 = s.toString();
			final String s2 = "state=" + s.getState();	
			final StringBuilder sb = new StringBuilder();
			// TODO better: String.format();
			final Formatter f = new Formatter();
			sb.append(f.format("\t%-28s\t%-22s", s1, s2).toString());
			
			if (lastChangedStep != null && lastChangedStep.equals(s)) {
				sb.append("\t(changed)");
			}
			logger.info(this, sb.toString());
		}
		logger.info(this, POST_LINE);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
