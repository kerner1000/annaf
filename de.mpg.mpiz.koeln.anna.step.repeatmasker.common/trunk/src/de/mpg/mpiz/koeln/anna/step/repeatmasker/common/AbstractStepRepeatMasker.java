package de.mpg.mpiz.koeln.anna.step.repeatmasker.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff.element.NewGFFElement;
import de.bioutils.gff.file.*;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.AbstractStep;
import de.mpg.mpiz.koeln.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

public abstract class AbstractStepRepeatMasker extends AbstractStep {
	
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
			throw new StepExecutionException(
					"cannot access repeatmasker working dir");
		if (!FileUtils.dirCheck(workingDir, true))
			throw new StepExecutionException("cannot access step working dir");
	}

	private void printProperties() {
		logger.debug(this, " created, properties:");
		logger.debug(this, "\tstepWorkingDir=" + workingDir);
		logger.debug(this, "\texeDir=" + exeDir);
	}

	public boolean canBeSkipped(DataProxy data)
			throws StepExecutionException {
		try {
			// must this two actions be atomar?
			final boolean repeatGtf = (data.getRepeatMaskerGff() != null);
			final boolean repeatGtfSize = (data
					.getRepeatMaskerGff().size() != 0);
			
			return (repeatGtf && repeatGtfSize);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}

	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		try {
			// must this two actions be atomar?
			final boolean sequence = (data.getInputSequences() != null);
			final boolean sequenceSize = (data
					.getInputSequences().size() != 0);
			
			return (sequence && sequenceSize);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}
	
	public boolean run(DataProxy data, StepProcessObserver listener)
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
			new NewFASTAFileImpl(data.getInputSequences())
			.write(inFile);
			worker.addResultFile(true, outFile);
		success = worker.createAndStartProcess();
		if (success) {
			upUpdate(data, outFile);
		}
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
		return success;
	}
	
	private void upUpdate(DataProxy data, File outFile) throws DataBeanAccessException, IOException, GFFFormatErrorException{
		logger.debug(this, "updating data");
		data.setRepeatMaskerGff(
				(ArrayList<? extends NewGFFElement>) NewGFFFileImpl.parseFile(outFile).getElements());
	}
	
	protected abstract AbstractStepProcessBuilder getProcess(File inFile);

}
