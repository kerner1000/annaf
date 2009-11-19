package de.mpg.mpiz.koeln.anna.core.events;

import java.util.Collection;

import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

/**
 * 
 * @lastVisit 2009-09-22
 * @author Alexander Kerner
 *
 */
public class StepStateChangeEvent extends AnnaEvent {

	private static final long serialVersionUID = 4513665226131110643L;
	private final AnnaStep step;

	public StepStateChangeEvent(Object source, Collection<AnnaStep> steps, AnnaStep step) {
		super(source, steps);
		this.step = step;
	}

	public AnnaStep getStep() {
		return step;
	}

	public State getState() {
		return step.getState();
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append(super.toString()).append("[").append(
				"step").append("=").append(step).append("]").append("[").append(
				"state").append("=").append(step.getState()).append("]").toString();
	}

}
