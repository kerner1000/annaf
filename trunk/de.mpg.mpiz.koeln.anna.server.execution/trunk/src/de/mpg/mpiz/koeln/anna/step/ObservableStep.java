package de.mpg.mpiz.koeln.anna.step;

import java.util.Collection;
import java.util.List;

public interface ObservableStep {
	
	public enum State {
		LOOSE, 
		
		// active steps
		REGISTERED, CHECK_NEED_TO_RUN, RUNNING,
		
		// well... might wait forever.
		WAIT_FOR_REQ, 
		
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
		
		public boolean isActive(){
			switch(this){
			case REGISTERED:
				return true;
			case CHECK_NEED_TO_RUN:
				return true;
			case RUNNING:
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
