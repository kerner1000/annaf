package de.mpg.mpiz.koeln.anna.step;

import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;

class DummyStep<V> extends AbstractStep<V> {
	
	private final String name;
	
	DummyStep(String name){
		this.name = name;
	}

	public boolean canBeSkipped(DataProxy<V> data) throws StepExecutionException {
		return true;
	}

	public boolean requirementsSatisfied(DataProxy<V> data)
			throws StepExecutionException {
		return true;
	}

	public boolean run(DataProxy<V> data, StepProcessObserver listener)
			throws StepExecutionException {
		return true;
	}
	
	public String toString() {
		return "(dummy)"+name;
	}

}
