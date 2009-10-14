package de.mpg.mpiz.koeln.anna.step;

import java.util.List;

public interface ObservableStep {
	
	public enum State {
		// non-finished steps
		LOOSE, REGISTERED, CHECK_NEED_TO_RUN, WAIT_FOR_REQ, RUNNING,
		
		// finished steps
		DONE, ERROR, SKIPPED;
		
		public boolean isFinished(){
			switch (this) {
			case DONE:
				return true;
			case ERROR:
				return true;
			case SKIPPED:
				return true;
			default:
				return false;
			}
		}
	}

	State getState();

	void setState(State state);

	List<String> requirementsNeeded();
	
}
