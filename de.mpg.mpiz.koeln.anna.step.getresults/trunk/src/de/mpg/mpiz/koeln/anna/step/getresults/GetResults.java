package de.mpg.mpiz.koeln.anna.step.getresults;

import java.io.File;

import de.bioutils.gff.file.NewGFFFile;
import de.bioutils.gff.file.NewGFFFileImpl;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;

public class GetResults extends AbstractGFF3AnnaStep {
	
	private final static String OUT_DIR_KEY = "anna.step.getResults.outDir";
	private final static String OUT_FILE_NAME_KEY = "anna.step.getResults.fileName";

	@Override
	public boolean run(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		boolean success = false;
		final File outDir = new File(super.getStepProperties().getProperty(
				OUT_DIR_KEY));
		success = FileUtils.dirCheck(outDir, true);
		writeFile(outDir);
		return success;
	}
	
	private void writeFile(File outDir) {
		final File outFile = new File(outDir, super.getStepProperties()
				.getProperty(OUT_FILE_NAME_KEY));
		logger.info(this, "writing results to "
				+ outFile);
		logger.info(this, " // not implemented \\ ");
	}

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

	public boolean isCyclic() {
		return true;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
