package de.mpg.mpiz.koeln.anna.listener.runtime.statistics;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import de.kerner.commons.file.FileUtils;
import de.mpg.koeln.anna.core.events.AnnaEvent;
import de.mpg.mpiz.koeln.anna.listener.abstractlistener.AbstractEventListener;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

public class Statistics extends AbstractEventListener {

	public static final TimeUnit TIMEUNIT = TimeUnit.MILLISECONDS;
	private final static String PRE_LINE = " RUNTIMES ++++++++++++++++++++++++++++++++++++++++++++++";
	private final static String POST_LINE = "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++";

	private final Map<AnnaStep, StateRuntimes> stepToStateRuntimes = new ConcurrentHashMap<AnnaStep, StateRuntimes>();

	public void eventOccoured(AnnaEvent event) {
		for (AnnaStep s : event.getRegisteredSteps()) {
			final State currentState = s.getState();
			if (stepToStateRuntimes.containsKey(s)) {
				final StateRuntimes r = stepToStateRuntimes.get(s);
				r.update(currentState);
			} else {
				stepToStateRuntimes.put(s, new StateRuntimes(logger));
			}
		}
		if (weAreDone(event))
			printStatistics();
	}

	private void printStatistics() {
		final StringBuilder sb = new StringBuilder();
		sb.append(PRE_LINE);
		sb.append(FileUtils.NEW_LINE);
		if (stepToStateRuntimes.isEmpty()) {
			// nothing
		} else {
			for (Entry<AnnaStep, StateRuntimes> e : stepToStateRuntimes
					.entrySet()) {
				final String s1 = e.getKey().toString();
				final String s2 = e.getValue().toString();
				sb.append(String.format("%-22s", s1));
				sb.append(FileUtils.NEW_LINE);
				if (!e.getValue().isEmpty())
					sb.append(String.format("%-22s", s2));
			}
		}
		sb.append(POST_LINE);
		logger.info(this, sb.toString());
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

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
