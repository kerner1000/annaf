package de.mpg.mpiz.koeln.anna.step.estreader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataModifier;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

public class ESTReader extends AbstractGFF3AnnaStep {

	private final static String INFILE_KEY = "anna.step.estreader.infile";

	@Override
	public boolean canBeSkipped(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		boolean b = true;
		try {
			final Collection<FASTAElement> c = proxy.viewData().getESTs();
			b = (c != null && c.size() != 0);
			logger.debug(this, "can be skipped=" + b + "(" + c.size() + ") est fastas retrieved");
		} catch (Exception e) {
			StepUtils.handleException(this, e);
		}
		return b;
	}

	@Override
	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		return true;
	}

	@Override
	public boolean run(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		try {
			final File file = new File(
					getStepProperties().getProperty(INFILE_KEY));
			logger.debug(this, "reading file " + file);
			final NewFASTAFile f = NewFASTAFileImpl.parse(file);
			logger.debug(this, "reading file " + file + " done, updating data");
			proxy.modifiyData(new DataModifier<GFF3DataBean>() {
				public void modifiyData(GFF3DataBean v) {
					v.setESTs(new ArrayList<FASTAElement>(f.getElements()));
					logger.debug(this, "updating data done");
				}
			});
			return true;
		} catch (Throwable e) {
			StepUtils.handleException(this, e);
			return false;
		}
	}

	public boolean isCyclic() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
