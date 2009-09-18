package de.mpg.mpiz.koeln.anna.step.conrad.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.element.NewGFFElement;
import de.bioutils.gff.file.NewGFFFile;
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
 * @Strings
 * @Excetions
 * @ThreadSave
 * @author Alexander Kerner
 * 
 */
public abstract class AbstractConradTrainStep extends AbstractConradStep {
	
	protected final static String TRAIN_PREFIX_KEY = "train.";
	protected final static String WORKING_DIR_KEY = ConradConstants.PROPERTIES_KEY_PREFIX
	+ TRAIN_PREFIX_KEY + "workingDir";
	protected File inFasta;
	protected File inGff;
	protected File trainingFile;

	@Override
	protected synchronized void init(BundleContext context)
			throws StepExecutionException {
		try {
			super.init(context);
			logger.debug(this, "doing initialisation");
			workingDir = new File(super.getStepProperties().getProperty(
					WORKING_DIR_KEY));
			logger.debug(this, StringUtils.getString("got working dir=",workingDir.getAbsolutePath()));
			if (!FileUtils.dirCheck(workingDir.getAbsoluteFile(), true))
				throw new FileNotFoundException(StringUtils.getString("cannot access working dir ", workingDir.getAbsolutePath()));
			process = getProcess();
			trainingFile = new File(workingDir, "trainingFile.bin");
			logger.debug(this, StringUtils.getString("init done: workingDir=", workingDir.getAbsolutePath()));
			logger.debug(this, StringUtils.getString("init done: trainingFile=", trainingFile.getAbsolutePath()));
			logger.debug(this, StringUtils.getString("init done: process=",process));
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
		}
		logger.debug(this, "initialisation done");
	}

	public boolean canBeSkipped(DataProxy data) throws StepExecutionException {
		try {
			final boolean trainingFile = (data.getConradTrainingFile() != null && data
					.getConradTrainingFile().exists());
			
			final boolean trainingFileRead = (data.getConradTrainingFile() != null && data
					.getConradTrainingFile().canRead());
			
			logger.debug(this, "need to run: trainingFile=" + trainingFile);
			logger.debug(this, "need to run: trainingFileRead="+ trainingFileRead);
			
			return trainingFile && trainingFileRead;
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}

	public boolean requirementsSatisfied(DataProxy data)
			throws StepExecutionException {
		try {
			final boolean fastas = (data.getVerifiedGenesFasta() != null);
			final boolean fastasSize = (data.getVerifiedGenesFasta().size() != 0);
			final boolean gtf = (data.getVerifiedGenesGff() != null);
			final boolean gtfSize = (data.getVerifiedGenesGff().size() != 0);
			logger.debug(this, "requirements: fastas=" + fastas);
			logger.debug(this, "requirements: fastasSize=" + fastasSize);
			logger.debug(this, "requirements: gtf=" + gtf);
			logger.debug(this, "requirements: gtfSize=" + gtfSize);
			return (fastas && fastasSize && gtf && gtfSize);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}

	private void createFiles(DataProxy data) throws DataBeanAccessException,
			IOException {
		try{
		inFasta = new File(workingDir, "ref.fasta");
		logger.debug(this, "ref.fasta=" + inFasta);
		
		logger.debug(this, "getting fastas for veryfied genes");
		final ArrayList<? extends FASTAElement> fastas = data
				.getVerifiedGenesFasta();
		
		final NewFASTAFile fastaFile = new NewFASTAFileImpl(fastas);
		
		logger.debug(this, "writing fastas to " + inFasta);
		fastaFile.write(inFasta);
		
		final File inGff = new File(workingDir, "ref.gtf");
		logger.debug(this, "ref.gtf=" + inGff);
		
		logger.debug(this, "getting gtfs for veryfied genes");
		final ArrayList<? extends NewGFFElement> gtfs = data.getVerifiedGenesGff();
		
		final NewGFFFile gtfFile = new NewGFFFileImpl(gtfs);
		
		logger.debug(this, "writing gtfs to " + inGff);
		gtfFile.write(inGff);
		
//		inFasta.deleteOnExit();
//		inGff.deleteOnExit();
		}catch(Throwable t){
			t.printStackTrace();
			System.exit(15);
		}
	}
	
	public boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException {
		logger.debug(this, "running");
		boolean success = true;
		try {
			logger.debug(this, "creating ref.* files");
			createFiles(data);
			logger.debug(this, "starting process");
			process.addResultFile(true, trainingFile.getAbsoluteFile());
			success = process.createAndStartProcess();
			if (success) {
				logger.debug(this, "process sucessfull, updating data bean");
				update(data);
			}
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
		logger.debug(this, "process sucessfull=" + success);
		return success;
	}

	protected void update(DataProxy data) throws DataBeanAccessException {
		data.setConradTrainingFile(trainingFile.getAbsoluteFile());
	}
}
