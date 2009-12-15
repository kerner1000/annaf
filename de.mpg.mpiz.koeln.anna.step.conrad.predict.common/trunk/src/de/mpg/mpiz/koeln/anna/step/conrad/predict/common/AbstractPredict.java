package de.mpg.mpiz.koeln.anna.step.conrad.predict.common;

import java.io.File;
import java.io.Serializable;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff3.GFF3Utils;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.element.GFF3ElementBuilder;
import de.bioutils.gff3.element.GFF3ElementGroup;
import de.bioutils.gff3.element.GFF3ElementGroupImpl;
import de.kerner.commons.StringUtils;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3WrapperStep;
import de.mpg.mpiz.koeln.anna.core.AnnaConstants;
import de.mpg.mpiz.koeln.anna.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataModifier;
import de.mpg.mpiz.koeln.anna.server.data.DataProxy;
import de.mpg.mpiz.koeln.anna.step.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.conrad.common.ConradConstants;

public abstract class AbstractPredict extends AbstractGFF3WrapperStep {

	protected final static String TRAIN_PREFIX_KEY = "predict.";
	protected volatile File resultFile = null;
	protected volatile File trainingFile = null;

	// Constructor //

	public AbstractPredict() {
		super(new File(ConradConstants.WORKING_DIR), new File(
				ConradConstants.WORKING_DIR));
	}

	// Private //

	private void createFiles(DataProxy<GFF3DataBean> data) throws Throwable {
		resultFile = new File(workingDir, "result.gtf");
		trainingFile = (File) data.viewData().getCustom(
				ConradConstants.TRAINING_FILE_KEY);
		final File file = new File(workingDir, "ref.fasta");
		new NewFASTAFileImpl(data.viewData().getInputSequence()).write(file);
		
		logger.debug(StringUtils.getString("got ", trainingFile,
				" as training file from data proxy (size=", trainingFile
						.length(), ")"));

		// copying does not work for some reason.
		// take "original" file for now

		// try {
		// new LazyFileCopier(file2, trainingFile).copy();
		// } catch (Throwable t) {
		// t.printStackTrace();
		// }
		// logger.debug(this, "copied files: old=" + file2.length() + ",new="
		// + trainingFile.length());
		// trainingFile.deleteOnExit();

	}

	// Override //

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	// Implement //

	@Override
	public void prepare(DataProxy<GFF3DataBean> data) throws Throwable {
		// its to late to do that here
		//createFiles(data);
	}

	@Override
	public void update(DataProxy<GFF3DataBean> data) throws Throwable {
		final GFF3ElementGroup c = GFF3Utils.convertFromGFFFile(resultFile,
				true).getElements();
		final GFF3ElementGroup result = new GFF3ElementGroupImpl(c.isSorted());
		for(GFF3Element e : c){
			result.add(new GFF3ElementBuilder(e).setSource(AnnaConstants.IDENT).build());
		}
		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				logger.debug("setting predicted genes");
				v.setPredictedGenesGFF(result);
			}
		});
	}

	@Override
	public boolean canBeSkipped(DataProxy<GFF3DataBean> data) throws Throwable {
		final boolean predictedGtf = (data.viewData().getPredictedGenesGFF() != null);
		final boolean predictedGtfSize = (!data.viewData()
				.getPredictedGenesGFF().isEmpty());
		logger.debug(StringUtils.getString("need to run: predictedGtf=",
				predictedGtf));
		logger.debug(StringUtils.getString("need to run: predictedGtfSize=",
				predictedGtfSize));
		return (predictedGtf && predictedGtfSize);

	}

	@Override
	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data)
			throws Throwable {

		final boolean trainingFileNotNull = (data.viewData().getCustom(ConradConstants.TRAINING_FILE_KEY) != null);
		if(trainingFileNotNull){
			final File trainingFileFile = (File) data.viewData().getCustom(ConradConstants.TRAINING_FILE_KEY);
			
			if(!FileUtils.fileCheck(trainingFileFile, false)){
				logger.info("cannot access training file \"" + trainingFileFile + "\"");
				return false;
			}
			
			// at this point training file is there and accessible
			final boolean inputSequences = (data.viewData().getInputSequence() != null);
			final boolean inputSequencesSize = (!data.viewData().getInputSequence()
					.isEmpty());
			logger.debug(StringUtils.getString("requirements: trainingFile=",
					trainingFileFile));
			logger.debug(StringUtils.getString("requirements: inputSequences=",
					inputSequences));
			logger.debug(StringUtils.getString("requirements: inputSequencesSize=",
					inputSequencesSize));
			
			return (inputSequences && inputSequencesSize);
			
		} else {
			logger.debug(StringUtils.getString("requirements: trainingFileNotNull=",
					trainingFileNotNull));
			return false;
		}

		

	}

	@Override
	public boolean run(DataProxy<GFF3DataBean> data) throws Throwable {
		createFiles(data);
		addShortCutFile(resultFile.getAbsoluteFile());
		addResultFileToWaitFor(resultFile.getAbsoluteFile());
		return start();
	}

	public boolean isCyclic() {
		return false;
	}

}
