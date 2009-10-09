package de.mpg.mpiz.koeln.anna.step;

import java.util.List;

public interface ObservableStep {
	
	public enum State {
		// non-finished steps
		LOOSE, REGISTERED, CHECK_NEED_TO_RUN, WAIT_FOR_REQ, RUNNING,
		
		// finished steps
		DONE, ERROR, SKIPPED
	}

	State getState();

	void setState(State state);

	List<String> requirementsNeeded() throws Exception;
	
}
