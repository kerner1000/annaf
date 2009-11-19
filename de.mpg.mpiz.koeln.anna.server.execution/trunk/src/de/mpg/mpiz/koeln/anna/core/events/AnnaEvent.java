package de.mpg.mpiz.koeln.anna.core.events;

import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import de.mpg.mpiz.koeln.anna.step.AnnaStep;

/**
 * 
 * @lastVisit 2009-09-22
 * @author Alexander Kerner
 *
 */
public class AnnaEvent extends EventObject {

	private static final long serialVersionUID = 1551843380559471696L;
	private final Set<AnnaStep> registeredSteps = new HashSet<AnnaStep>();
	
	public AnnaEvent(Object source, Collection<? extends AnnaStep> registeredSteps) {
		super(source);
		this.registeredSteps.clear();
		this.registeredSteps.addAll(registeredSteps);
	}
	
	public Collection<AnnaStep> getRegisteredSteps(){
		return new HashSet<AnnaStep>(registeredSteps);
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("[").append(
		"stepReg").append("=").append(registeredSteps).append("]").toString();
	}
}
