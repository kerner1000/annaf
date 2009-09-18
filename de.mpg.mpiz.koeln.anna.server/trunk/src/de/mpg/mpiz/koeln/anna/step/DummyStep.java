package de.mpg.mpiz.koeln.anna.step;

import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;

class DummyStep extends AbstractGFF3Step {
	
	private final String name;
	
	DummyStep(String name){
		this.name = name;
	}
	
	public String toString() {
		return "(dummy)"+name;
	}

	public boolean canBeSkipped(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		return true;
	}

	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		return true;
	}

	public boolean run(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		return true;
	}

	public boolean run(DataProxy<GFF3DataBean> data,
			StepProcessObserver listener) throws StepExecutionException {
		return true;
	}

}
