package de.mpg.mpiz.koeln.anna.abstractstep;

import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;

class DummyStep extends AbstractGFF3AnnaStep {
	
	private final String name;
	
	DummyStep(String name){
		this.name = name;
	}
	
	public String toString() {
		return "(dummy)"+name;
	}

	@Override
	public boolean canBeSkipped(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		return true;
	}

	@Override
	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		return true;
	}

	@Override
	public boolean run(DataProxy<GFF3DataBean> proxy) throws StepExecutionException {
		// TODO Auto-generated method stub
		return false;
	}
}
