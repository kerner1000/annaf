package de.mpg.mpiz.koeln.anna.step.repeatmasker.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataModifier;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;
import de.mpg.mpiz.koeln.anna.step.repeatmasker.adapter.ResultsPreprocessor;

public abstract class AbstractStepRepeatMasker extends AbstractGFF3AnnaStep {
	
	protected File exeDir;
	protected File workingDir;

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected synchronized void init(BundleContext context) throws StepExecutionException {
		super.init(context);
		assignProperties();
		validateProperties();
		printProperties();
	}

	private void assignProperties() {
		exeDir = new File(getStepProperties().getProperty(RepeatMaskerConstants.EXE_DIR_KEY));
		workingDir = new File(getStepProperties().getProperty(RepeatMaskerConstants.WORKING_DIR_KEY));
	}

	private void validateProperties() throws StepExecutionException {
		if (!FileUtils.dirCheck(exeDir, false))
			throw new StepExecutionException(this, 
					"cannot access repeatmasker working dir");
		if (!FileUtils.dirCheck(workingDir, true))
			throw new StepExecutionException(this, "cannot access step working dir");
	}

	private void printProperties() {
		logger.debug(this, " created, properties:");
		logger.debug(this, "\tstepWorkingDir=" + workingDir);
		logger.debug(this, "\texeDir=" + exeDir);
	}

	public boolean canBeSkipped(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		try {
			// must this two actions be atomar?
			final boolean repeatGtf = (data.viewData().getRepeatMaskerGFF() != null);
			final boolean repeatGtfSize = (data.viewData()
					.getRepeatMaskerGFF().size() != 0);
			
			return (repeatGtf && repeatGtfSize);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}
	
	@Override
	public List<String> requirementsNeeded(DataProxy<GFF3DataBean> data)
			throws Exception {
		final List<String> r = new ArrayList<String>();
		final boolean sequence = (data.viewData().getInputSequence() != null);
		final boolean sequenceSize = (data.viewData()
				.getInputSequence().size() != 0);
		if(!sequence || !sequenceSize){
			r.add("input sequences");
		}
		return r;
	}

	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		try {
			// must this two actions be atomar?
			final boolean sequence = (data.viewData().getInputSequence() != null);
			final boolean sequenceSize = (data.viewData()
					.getInputSequence().size() != 0);
			
			return (sequence && sequenceSize);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}
	
	public boolean run(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		logger.debug(this, "running");
		final File inFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME);
		final File outFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME
				+ RepeatMaskerConstants.OUTFILE_POSTFIX);
		logger.debug(this, "inFile="+inFile);
		logger.debug(this, "outFile="+outFile);
		final AbstractStepProcessBuilder worker = getProcess(inFile);
		boolean success = true;
		try{
			new NewFASTAFileImpl(data.viewData().getInputSequence())
			.write(inFile);
			worker.addResultFile(true, outFile);
		success = worker.createAndStartProcess();
		if (success) {
			update(data, outFile);
		}
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
		return success;
	}
	
	private void update(DataProxy<GFF3DataBean> data, final File outFile) throws DataBeanAccessException, IOException, GFFFormatErrorException{
		logger.debug(this, "updating data");
		final ArrayList<GFF3Element> result = new ArrayList<GFF3Element>();
		new ResultsPreprocessor().process(outFile, outFile);
		result.addAll(GFF3FileImpl.convertFromGFF(outFile).getElements());
		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				v.setRepeatMaskerGFF(result);	
			}
		});
	}
	
	public boolean isCyclic() {
		return false;
	}
	
	protected abstract AbstractStepProcessBuilder getProcess(File inFile);

}
