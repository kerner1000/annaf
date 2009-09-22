package de.mpg.mpiz.koeln.anna.step.getresults;

import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;

public class GetResults extends AbstractGFF3AnnaStep {

	@Override
	public boolean canBeSkipped(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		return false;
	}

	@Override
	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		return true;
	}

	@Override
	public boolean run(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		logger.debug(this, "IM RUNNING!!");
		return false;
	}

	public boolean isCyclic() {
		return true;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
