package de.mpg.mpiz.koeln.anna.step;

public interface ObservableStep {
	
	public enum State {
		LOOSE, REGISTERED, CHECK_NEED_TO_RUN, WAIT_FOR_REQ, RUNNING, DONE,
		ERROR, SKIPPED
	}

	State getState();

	void setState(State state);

}
