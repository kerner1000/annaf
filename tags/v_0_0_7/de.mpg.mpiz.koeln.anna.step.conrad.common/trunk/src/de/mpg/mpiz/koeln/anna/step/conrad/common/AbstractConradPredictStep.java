package de.mpg.mpiz.koeln.anna.step.conrad.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.kerner.commons.StringUtils;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataModifier;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
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
			logger.debug(this, StringUtils.getString("got working dir=",
					workingDir.getAbsolutePath()));
			if (!FileUtils.dirCheck(workingDir.getAbsoluteFile(), true))
				throw new FileNotFoundException(StringUtils.getString(
						"cannot access working dir ", workingDir
								.getAbsolutePath()));
			process = getProcess();
			trainingFile = new File(workingDir, "trainingFile.bin");
			resultFile = new File(workingDir, "result.gtf");
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
	}

	@Override
	public boolean canBeSkipped(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		try {
			final boolean predictedGtf = (data.viewData()
					.getPredictedGenesGFF() != null);
			final boolean predictedGtfSize = (data.viewData()
					.getPredictedGenesGFF().size() != 0);
			logger.debug(this, StringUtils.getString(
					"need to run: predictedGtf=", predictedGtf));
			logger.debug(this, StringUtils.getString(
					"need to run: predictedGtfSize=", predictedGtfSize));
			return (predictedGtf && predictedGtfSize);
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
			final boolean trainingFile = (data.viewData().getCustom(
					TRAINING_FILE) != null && ((File) data.viewData()
					.getCustom(TRAINING_FILE)).exists());

			final boolean trainingFileRead = (data.viewData()
					.getCustom(TRAINING_FILE)) != null
					&& ((File) data.viewData().getCustom(TRAINING_FILE))
							.canRead();

			final boolean inputSequences = (data.viewData().getInputSequence() != null);
			final boolean inputSequencesSize = (data.viewData()
					.getInputSequence().size() != 0);

			logger.debug(this, StringUtils.getString(
					"requirements: trainingFile=", data.viewData().getCustom(
							TRAINING_FILE)));
			logger.debug(this, StringUtils.getString(
					"requirements: trainingFile=", trainingFile));
			logger.debug(this, StringUtils.getString(
					"requirements: trainingFileRead=", trainingFileRead));
			logger.debug(this, StringUtils.getString(
					"requirements: inputSequences=", inputSequences));
			logger.debug(this, StringUtils.getString(
					"requirements: inputSequencesSize=", inputSequencesSize));

			return (trainingFile && trainingFileRead && inputSequences && inputSequencesSize);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}

	@Override
	public boolean run(DataProxy<GFF3DataBean> data)
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

	private void update(File resultFile, DataProxy<GFF3DataBean> data)
			throws IOException, GFFFormatErrorException,
			DataBeanAccessException {
		final Collection<? extends GFF3Element> c = GFF3FileImpl
				.convertFromGFF(resultFile).getElements();
		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				v.setPredictedGenesGFF(new ArrayList<GFF3Element>(c));
			}
		});
	}

	private void createFiles(DataProxy<GFF3DataBean> data)
			throws DataBeanAccessException, IOException {

		final File file = new File(workingDir, "ref.fasta");
		new NewFASTAFileImpl(data.viewData().getInputSequence()).write(file);

		final File file2 = (File) data.viewData().getCustom(TRAINING_FILE);
		logger.debug(this, StringUtils
				.getString("got ", file2,
						" as training file from data proxy (size=", file2
								.length(), ")"));

		// copying does not work for some reason.
		// take "original" file for now
		trainingFile = file2;

		// try {
		// new LazyFileCopier(file2, trainingFile).copy();
		// } catch (Throwable t) {
		// t.printStackTrace();
		// }
		// logger.debug(this, "copied files: old=" + file2.length() + ",new="
		// + trainingFile.length());
		// trainingFile.deleteOnExit();
		file.deleteOnExit();
	}
}
