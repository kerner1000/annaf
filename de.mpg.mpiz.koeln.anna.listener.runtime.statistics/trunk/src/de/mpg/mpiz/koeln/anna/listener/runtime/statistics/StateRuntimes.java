package de.mpg.mpiz.koeln.anna.listener.runtime.statistics;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import de.kerner.commons.StopWatch;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

class StateRuntimes {

	private final static Log log = new Log(StateRuntimes.class);
	private volatile State lastState;
	private final Map<State, StopWatch> stateToStopWatch = new ConcurrentHashMap<State, StopWatch>();

	StateRuntimes(LogDispatcher logger) {
		this.logger = logger;
	}
	
	boolean isEmpty(){
		return stateToStopWatch.isEmpty();
	}

	void update(State state) {
		if (stateToStopWatch.containsKey(state)) {
			// current state already registered and stop watch should be
			// running, ignore.
			return;
		}
		// never have been in this state before, take time from last state and
		// create new stop watch for new state.
		if (lastState != null) {
			final StopWatch oldWatch = stateToStopWatch.get(lastState);
			if (oldWatch.isRunning()) {
				// all good, get time and forget about this state.
				oldWatch.stop();
			} else {
				logger.warn(this,
						"inconsitent time measuring, stopwatch for state "
								+ state + " not running!");
			}
		}

		final StopWatch newWatch = new StopWatch();
		if(!state.isFinished())
		newWatch.start();
		stateToStopWatch.put(state, newWatch);
		this.lastState = state;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (Entry<State, StopWatch> e : stateToStopWatch.entrySet()) {
			final String s1 = e.getKey().toString();
			final long s2 = e.getValue().getElapsedTime(TimeUnit.MILLISECONDS)
					.getDuration(TimeUnit.MILLISECONDS);
			final String s3 = Boolean.toString(e.getValue().isRunning());
			sb.append(String.format(
					"\tstate\t%-22s\ttime[millisec]\t%,10d\trunning\t%6s", s1,
					s2, s3).toString());
			sb.append(FileUtils.NEW_LINE);
		}
		return sb.toString();
	}
}
