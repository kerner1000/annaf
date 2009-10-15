package de.mpg.mpiz.koeln.anna.step.repeatmasker.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3WrapperStep;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataModifier;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;
import de.mpg.mpiz.koeln.anna.step.repeatmasker.adapter.ResultsPreprocessor;

public abstract class AbstractStepRepeatMasker extends AbstractGFF3WrapperStep {

	protected volatile File outFile;
	protected volatile File inFile;
	protected volatile File outStr;
	
	@Override
	public void init(BundleContext context) throws StepExecutionException {
		super.init(context);
		exeDir = new File(getStepProperties().getProperty(RepeatMaskerConstants.EXE_DIR_KEY));
		workingDir = new File(getStepProperties().getProperty(RepeatMaskerConstants.WORKING_DIR_KEY));
		logger.debug(this, "exeDir="+exeDir.getAbsolutePath());
		logger.debug(this, "workingDir="+workingDir.getAbsolutePath());
		inFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME);
		outFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME
				+ RepeatMaskerConstants.OUTFILE_POSTFIX);
		outStr = new File(workingDir, getStepProperties().getProperty(RepeatMaskerConstants.OUTSTREAM_FILE_KEY));
	}
	
	@Override
	protected void createIfAbsend() throws StepExecutionException {
		super.createIfAbsend();
		if (!FileUtils.fileCheck(inFile, true))
			throw new StepExecutionException(this,
					"cannot create file \"" + inFile + "\"");
		if (!FileUtils.fileCheck(outFile, true))
			throw new StepExecutionException(this,
					"cannot create file \"" + outFile + "\"");
		if (!FileUtils.fileCheck(outStr, true))
			throw new StepExecutionException(this,
					"cannot create file \"" + outStr + "\"");
		
	}

	@Override
	public void prepare(DataProxy<GFF3DataBean> data) throws Exception {
		new NewFASTAFileImpl(data.viewData().getInputSequence())
		.write(inFile);
	}
	
	@Override
	public boolean update(DataProxy<GFF3DataBean> data) throws StepExecutionException {
		try {
		logger.debug(this, "updating data");
		final ArrayList<GFF3Element> result = new ArrayList<GFF3Element>();
		new ResultsPreprocessor().process(outFile, outFile);
		result.addAll(GFF3FileImpl.convertFromGFF(outFile).getElements());
		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				v.setRepeatMaskerGFF(result);	
			}
		});
		return true;
		} catch (Exception e) {
			StepUtils.handleException(this, e);
			return false;
		}
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
		logger.debug(this, "running" + FileUtils.NEW_LINE + "\tinFile="+inFile + FileUtils.NEW_LINE + "\toutFile="+outFile);
		boolean success = true;
		try{
			addResultFileToWaitFor(outFile);
			addShortCutFile(outFile);
			redirectOutStreamToFile(outStr);
			success = start();
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
		return success;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	public boolean isCyclic() {
		return false;
	}

}
