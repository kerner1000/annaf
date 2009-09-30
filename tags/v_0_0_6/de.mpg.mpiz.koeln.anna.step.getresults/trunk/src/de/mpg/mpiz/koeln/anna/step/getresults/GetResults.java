package de.mpg.mpiz.koeln.anna.step.getresults;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

public class GetResults extends AbstractGFF3AnnaStep {

	private final static String OUT_DIR_KEY = "anna.step.getResults.outDir";
	private final static String OUT_FILE_NAME_KEY = "anna.step.getResults.fileName";

	@Override
	public boolean run(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		boolean success = false;
		final File outDir = new File(super.getStepProperties().getProperty(
				OUT_DIR_KEY));
		logger.debug(this, "got outdir=" + outDir);
		success = FileUtils.dirCheck(outDir, true);
		try {
			writeFile(outDir, proxy);
		} catch (Exception e) {
			StepUtils.handleException(this, e);
		}
		return success;
	}

	private void writeFile(File outDir, DataProxy<GFF3DataBean> proxy)
			throws DataBeanAccessException, IOException {
		logger.debug(this, "retrieving GFF for predicted genes");
		final Collection<GFF3Element> predicted = proxy.viewData()
				.getPredictedGenesGFF();
		logger.debug(this, "retrieving GFF for predicted genes done (elements="
				+ predicted.size() + ")");
		logger.debug(this, "retrieving GFF for repetetive elements");
		final Collection<GFF3Element> repeat = proxy.viewData()
				.getRepeatMaskerGFF();
		logger.debug(this,
				"retrieving GFF for repetetive elements done (elements="
						+ repeat.size() + ")");
		logger.debug(this, "merging");
		final Collection<GFF3Element> merged = new ArrayList<GFF3Element>();
		if(predicted.size() != 0)
			merged.addAll(predicted);
		if(repeat.size() != 0)
			merged.addAll(repeat);
		logger.debug(this, "merging done (elements="
						+ merged.size() + ")");
		if(merged.size() == 0){
			logger.debug(this, "nothing to write");
			return;
		}
		final File outFile = new File(outDir, super.getStepProperties()
				.getProperty(OUT_FILE_NAME_KEY));
		logger.info(this, "writing results to " + outFile);
		new GFF3FileImpl(merged).write(outFile);
		logger.debug(this, "done writing results to " + outFile);
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
