package de.mpg.mpiz.koeln.anna.listener.statistics.prediction;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import de.kerner.commons.file.FileUtils;
import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.core.events.AnnaEvent;
import de.mpg.mpiz.koeln.anna.listener.abstractlistener.AbstractEventListener;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;

public class PredictionStatistics extends AbstractEventListener {
	
	private final static Log logger = new Log(PredictionStatistics.class);
											 
	private final static String PRE_LINE = " PREDICTS +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++";
	private final static String POST_LINE = "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++";

	public void eventOccoured(AnnaEvent event) {
		if(weAreDone(event)){
			final StringBuilder sb = new StringBuilder();
			sb.append(PRE_LINE);
			sb.append(FileUtils.NEW_LINE);
		} else {
			// nothing
		}
		
	}
	
	private boolean weAreDone(AnnaEvent event) {
		final Collection<AnnaStep> eventList = event.getRegisteredSteps();
		for (AnnaStep s : eventList) {
			if (!(s.getState().isFinished())) {
				return false;
			}
		}
		return true;
	}

}
