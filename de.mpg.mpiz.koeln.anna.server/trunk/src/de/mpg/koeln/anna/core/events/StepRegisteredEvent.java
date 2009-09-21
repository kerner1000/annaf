package de.mpg.koeln.anna.core.events;

import de.mpg.mpiz.koeln.anna.step.AnnaStep;

public class StepRegisteredEvent extends AnnaEvent {

	private static final long serialVersionUID = -8941513461979205486L;
	private final AnnaStep step;

	public StepRegisteredEvent(Object source, AnnaStep step) {
		super(source);
		this.step = step;
	}

	public AnnaStep getStep() {
		return step;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(super.toString()).append("[").append(
				"step").append("=").append(step).append("]").toString();
	}

}
