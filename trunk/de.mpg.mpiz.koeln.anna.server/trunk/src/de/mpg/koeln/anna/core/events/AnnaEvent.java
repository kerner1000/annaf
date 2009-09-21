package de.mpg.koeln.anna.core.events;

import java.util.EventObject;

public class AnnaEvent extends EventObject {

	private static final long serialVersionUID = 1551843380559471696L;

	public AnnaEvent(Object source) {
		super(source);
	}
	
	@Override
	public String toString() {
		return super.toString().substring(super.toString().lastIndexOf("."));
	}
}
