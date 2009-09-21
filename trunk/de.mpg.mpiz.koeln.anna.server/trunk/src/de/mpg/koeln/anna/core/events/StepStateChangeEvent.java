package de.mpg.koeln.anna.core.events;

import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

public class StepStateChangeEvent extends AnnaEvent {

	private static final long serialVersionUID = 4513665226131110643L;
	private final AnnaStep step;
	private final State state;

	public StepStateChangeEvent(Object source, AnnaStep step, State state) {
		super(source);
		this.step = step;
		this.state = state;
	}

	public AnnaStep getStep() {
		return step;
	}

	public State getState() {
		return state;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append(super.toString()).append("[").append(
				"step").append("=").append(step).append("]").append("[").append(
				"state").append("=").append(state).append("]").toString();
	}

}
