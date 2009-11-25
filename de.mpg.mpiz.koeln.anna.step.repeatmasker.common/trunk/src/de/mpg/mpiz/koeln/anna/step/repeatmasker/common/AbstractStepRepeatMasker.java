package de.mpg.mpiz.koeln.anna.step.repeatmasker.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff3.GFF3Utils;
import de.bioutils.gff3.Type;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.element.GFF3ElementBuilder;
import de.bioutils.gff3.element.GFF3ElementGroup;
import de.bioutils.gff3.element.GFF3ElementGroupImpl;
import de.bioutils.gff3.file.GFF3File;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3WrapperStep;
import de.mpg.mpiz.koeln.anna.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataModifier;
import de.mpg.mpiz.koeln.anna.server.data.DataProxy;
import de.mpg.mpiz.koeln.anna.step.StepExecutionException;

/**
 * 
 * @author Alexander Kerner
 * 
 */
public abstract class AbstractStepRepeatMasker extends AbstractGFF3WrapperStep {

	protected volatile File outFile;
	protected volatile File inFile;
	protected volatile File outStr;

	// Override //

	@Override
	public void init(BundleContext context) throws StepExecutionException {
		super.init(context);
		exeDir = new File(getStepProperties().getProperty(
				RepeatMaskerConstants.EXE_DIR_KEY));
		workingDir = new File(getStepProperties().getProperty(
				RepeatMaskerConstants.WORKING_DIR_KEY));
		logger.debug("exeDir=" + exeDir.getAbsolutePath());
		logger.debug("workingDir=" + workingDir.getAbsolutePath());
		inFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME);
		outFile = new File(workingDir, RepeatMaskerConstants.TMP_FILENAME
				+ RepeatMaskerConstants.OUTFILE_POSTFIX);
		outStr = new File(workingDir, getStepProperties().getProperty(
				RepeatMaskerConstants.OUTSTREAM_FILE_KEY));
	}

	@Override
	public List<String> requirementsNeeded(DataProxy<GFF3DataBean> data)
			throws Exception {
		final List<String> r = new ArrayList<String>();
		final boolean sequence = (data.viewData().getInputSequence() != null);
		final boolean sequenceSize = (!data.viewData().getInputSequence()
				.isEmpty());
		if (!sequence || !sequenceSize) {
			r.add("input sequences");
		}
		return r;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	// Implement //

	public void prepare(DataProxy<GFF3DataBean> data) throws Throwable {
		new NewFASTAFileImpl(data.viewData().getInputSequence()).write(inFile);

//		if (!FileUtils.fileCheck(inFile, true))
//			throw new StepExecutionException(this, "cannot access file \""
//					+ inFile + "\"");
//		if (!FileUtils.fileCheck(outFile, true))
//			throw new StepExecutionException(this, "cannot access file \""
//					+ outFile + "\"");
//		if (!FileUtils.fileCheck(outStr, true))
//			throw new StepExecutionException(this, "cannot access file \""
//					+ outStr + "\"");

	}

	public void update(DataProxy<GFF3DataBean> data) throws Throwable {
		logger.debug("updating data");
		final ResultsPreprocessor p = new ResultsPreprocessor();
		logger.debug("postprocessing results");
		p.process(outFile, outFile);

		// TODO really sorted?
		GFF3File tmp = GFF3Utils.convertFromGFFFile(outFile, true);
		final GFF3ElementGroup result = new GFF3ElementGroupImpl(true);
		logger.debug("changing type from \""
				+ tmp.getElements().iterator().next().getType() + "\" to \""
				+ Type.repeat_region + "\"");
		for (GFF3Element e : tmp.getElements()) {
			result.add(new GFF3ElementBuilder(e).setType(Type.repeat_region)
					.build());
		}

		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				// TODO do we really know elements are sorted??
				v.setRepeatMaskerGFF(result);
			}
		});
	}

	public boolean canBeSkipped(DataProxy<GFF3DataBean> data) throws Throwable {
		// must this two actions be atomar?
		final boolean repeatGtf = (data.viewData().getRepeatMaskerGFF() != null);
		final boolean repeatGtfSize = (!data.viewData().getRepeatMaskerGFF()
				.isEmpty());
		return (repeatGtf && repeatGtfSize);
	}

	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data)
			throws Throwable {
		// must this two actions be atomar?
		final boolean sequence = (data.viewData().getInputSequence() != null);
		final boolean sequenceSize = (!data.viewData().getInputSequence()
				.isEmpty());
		return (sequence && sequenceSize);
	}

	@Override
	public boolean run(DataProxy<GFF3DataBean> data) throws Throwable {
		logger.debug("running" + FileUtils.NEW_LINE + "\tinFile=" + inFile
				+ FileUtils.NEW_LINE + "\toutFile=" + outFile);
		boolean success = true;
		addResultFileToWaitFor(outFile);
		addShortCutFile(outFile);
		redirectOutStreamToFile(outStr);
		success = start();
		return success;
	}

	public boolean isCyclic() {
		return false;
	}

}
