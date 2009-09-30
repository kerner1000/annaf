package de.mpg.mpiz.koeln.anna.step.conrad.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.file.NewGFFFile;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.kerner.commons.StringUtils;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataModifier;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;
import de.mpg.mpiz.koeln.anna.step.conrad.data.adapter.GFF3ConverterImpl;

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
			logger.debug(this, StringUtils.getString("got working dir=",
					workingDir.getAbsolutePath()));
			if (!FileUtils.dirCheck(workingDir.getAbsoluteFile(), true))
				throw new FileNotFoundException(StringUtils.getString(
						"cannot access working dir ", workingDir
								.getAbsolutePath()));
			process = getProcess();
			trainingFile = new File(workingDir, "trainingFile.bin");
			logger.debug(this, StringUtils.getString("init done: workingDir=",
					workingDir.getAbsolutePath()));
			logger
					.debug(this, StringUtils.getString(
							"init done: trainingFile=", trainingFile
									.getAbsolutePath()));
			logger.debug(this, StringUtils.getString("init done: process=",
					process));
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
		}
		logger.debug(this, "initialisation done");
	}

	@Override
	public boolean canBeSkipped(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		try {
			final boolean trainingFile = (data.viewData().getCustom().get(
					TRAINING_FILE) != null && ((File) data.viewData()
					.getCustom().get(TRAINING_FILE)).exists());

			final boolean trainingFileRead = (data.viewData().getCustom().get(
					TRAINING_FILE) != null && ((File) data.viewData()
							.getCustom().get(TRAINING_FILE)).canRead());

			logger.debug(this, "need to run: trainingFile=" + trainingFile);
			logger.debug(this, "need to run: trainingFileRead="
					+ trainingFileRead);

			return trainingFile && trainingFileRead;
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}

	@Override
	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		try {
			final boolean fastas = (data.viewData().getVerifiedGenesFasta() != null);
			final boolean fastasSize = (data.viewData().getVerifiedGenesFasta().size() != 0);
			final boolean gtf = (data.viewData().getVerifiedGenesGFF() != null);
			final boolean gtfSize = (data.viewData().getVerifiedGenesGFF().size() != 0);
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

	private void createFiles(DataProxy<GFF3DataBean> data) throws DataBeanAccessException,
			IOException {
		try {
			createFastas(data);
			createGFFs(data);

			// inFasta.deleteOnExit();
			// inGff.deleteOnExit();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(15);
		}
	}

	private void createGFFs(DataProxy<GFF3DataBean> data) throws DataBeanAccessException, IOException {
		final File inGff = new File(workingDir, "ref.gtf");
		logger.debug(this, "ref.gtf=" + inGff);

		logger.debug(this, "getting gtfs for veryfied genes");
		final Collection<? extends GFF3Element> gtfs = data.viewData()
				.getVerifiedGenesGFF();

		final GFF3File gtfFile = new GFF3FileImpl(gtfs);
		final NewGFFFile newFile = new GFF3ConverterImpl().convert(gtfFile);

		logger.debug(this, "writing gtfs to " + inGff);
		newFile.write(inGff);
		
	}

	private void createFastas(DataProxy<GFF3DataBean> data) throws DataBeanAccessException, IOException {
		inFasta = new File(workingDir, "ref.fasta");
		logger.debug(this, "ref.fasta=" + inFasta);

		logger.debug(this, "getting fastas for veryfied genes");
		final Collection<? extends FASTAElement> fastas = data.viewData()
				.getVerifiedGenesFasta();

		final NewFASTAFile fastaFile = new NewFASTAFileImpl(fastas);

		logger.debug(this, "writing fastas to " + inFasta);
		fastaFile.write(inFasta);
	}

	@Override
	public boolean run(DataProxy<GFF3DataBean> data)
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

	protected void update(DataProxy<GFF3DataBean> data) throws DataBeanAccessException {
		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				logger.debug(this, "using custom slot: key=" + TRAINING_FILE + ", value="+trainingFile.getAbsoluteFile());
				v.getCustom().put(TRAINING_FILE, trainingFile.getAbsoluteFile());	
			}
		});
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
