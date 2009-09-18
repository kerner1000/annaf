package de.mpg.mpiz.koeln.anna.step.conrad.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.osgi.framework.BundleContext;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff.element.*;
import de.bioutils.gff.file.NewGFFFileImpl;
import de.kerner.commons.StringUtils;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

/**
 * @lastVisit 2009-08-12
 * @ThreadSave custom
 * @Exceptions all try-catch-throwable
 * @Strings good
 * @author Alexander Kerner
 * 
 */
public abstract class AbstractConradPredictStep extends AbstractConradStep {

	protected final static String TRAIN_PREFIX_KEY = "predict.";
	protected final static String WORKING_DIR_KEY = ConradConstants.PROPERTIES_KEY_PREFIX
			+ TRAIN_PREFIX_KEY + "workingDir";
	
	// assigned in init(), after that only read 
	protected File trainingFile;
	// assigned in init(), after that only read 
	protected File resultFile;

	@Override
	protected synchronized void init(BundleContext context)
			throws StepExecutionException {
		try {
			super.init(context);
			logger.debug(this, "doing initialisation");
			workingDir = new File(super.getStepProperties().getProperty(
					WORKING_DIR_KEY));
			logger.debug(this, StringUtils.getString("got working dir=", workingDir.getAbsolutePath()));
			if (!FileUtils.dirCheck(workingDir.getAbsoluteFile(), true))
				throw new FileNotFoundException(StringUtils.getString("cannot access working dir ",workingDir.getAbsolutePath()));
			process = getProcess();
			trainingFile = new File(workingDir, "trainingFile.bin");
			resultFile = new File(workingDir, "result.gtf");
			logger.debug(this, StringUtils.getString("init done: workingDir=",workingDir.getAbsolutePath()));
			logger.debug(this, StringUtils.getString("init done: trainingFile=",trainingFile.getAbsolutePath()));
			logger.debug(this, StringUtils.getString("init done: process=",process));
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
		}
	}

	public boolean canBeSkipped(DataProxy data) throws StepExecutionException {
		try {
			final boolean predictedGtf = (data.getPredictedGenesGtf() != null);
			final boolean predictedGtfSize = (data.getPredictedGenesGtf()
					.size() != 0);
			logger.debug(this, StringUtils.getString("need to run: predictedGtf=",predictedGtf));
			logger.debug(this, StringUtils.getString("need to run: predictedGtfSize=",predictedGtfSize));
			return (predictedGtf && predictedGtfSize);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}

	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		try {
			final boolean trainingFile = (data.getConradTrainingFile() != null && data
					.getConradTrainingFile().exists());

			final boolean trainingFileRead = (data.getConradTrainingFile() != null && data
					.getConradTrainingFile().canRead());

			final boolean inputSequences = (data.getInputSequences() != null);
			final boolean inputSequencesSize = (data.getInputSequences().size() != 0);

			logger.debug(this, StringUtils.getString("requirements: trainingFile=", trainingFile));
			logger.debug(this, StringUtils.getString("requirements: trainingFileRead=", trainingFileRead));
			logger.debug(this, StringUtils.getString("requirements: inputSequences=", inputSequences));
			logger.debug(this, StringUtils.getString("requirements: inputSequencesSize=", inputSequencesSize));

			return (trainingFile && trainingFileRead && inputSequences && inputSequencesSize);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}

	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		boolean success = true;
		try {
			createFiles(data);
			process.addResultFile(true, resultFile.getAbsoluteFile());
			success = process.createAndStartProcess();
			if (success)
				update(resultFile.getAbsoluteFile(), data);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
		return success;
	}

	private void update(File resultFile, DataProxy data) throws IOException,
			GFFFormatErrorException, DataBeanAccessException {
		final Collection<? extends NewGFFElement> c = NewGFFFileImpl.parseFile(resultFile)
				.getElements();
		data.setPredictedGenesGff(new ArrayList<NewGFFElement>(c));
	}

	private void createFiles(DataProxy data) throws DataBeanAccessException,
			IOException {

		final File file = new File(workingDir, "ref.fasta");
		new NewFASTAFileImpl(data.getInputSequences()).write(file);

		final File file2 = data.getConradTrainingFile();
		logger.debug(this, StringUtils.getString("got ", file2, " as training file from data proxy (size=", file2.length(), ")"));
		
		// copying does not work for some reason.
		// take "original" file for now
		trainingFile = file2;
		
//		try {
//			new LazyFileCopier(file2, trainingFile).copy();
//		} catch (Throwable t) {
//			t.printStackTrace();
//		}
//		logger.debug(this, "copied files: old=" + file2.length() + ",new="
//				+ trainingFile.length());
//		trainingFile.deleteOnExit();
		file.deleteOnExit();
	}
}
