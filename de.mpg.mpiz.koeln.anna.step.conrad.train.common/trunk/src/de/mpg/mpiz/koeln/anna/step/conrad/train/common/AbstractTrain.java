package de.mpg.mpiz.koeln.anna.step.conrad.train.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.bioutils.fasta.FASTAElementGroup;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff3.element.GFF3ElementGroup;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3WrapperStep;
import de.mpg.mpiz.koeln.anna.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataModifier;
import de.mpg.mpiz.koeln.anna.server.data.DataProxy;
import de.mpg.mpiz.koeln.anna.step.conrad.common.ConradConstants;

public abstract class AbstractTrain extends AbstractGFF3WrapperStep {

	protected final static File TRAINING_FILE = new File(
			ConradConstants.WORKING_DIR, "trainingFile.bin");

	public AbstractTrain() {
		super(new File(ConradConstants.WORKING_DIR), new File(
				ConradConstants.WORKING_DIR));
	}

	// Override //
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public List<String> requirementsNeeded(DataProxy<GFF3DataBean> data)
			throws Throwable {
		final List<String> r = new ArrayList<String>();
		final boolean fastas = (data.viewData().getVerifiedGenesFasta() != null);
		boolean fastasSize = false;
		if (fastas) {
			fastasSize = (!data.viewData().getVerifiedGenesFasta().isEmpty());
		}
		final boolean gtf = (data.viewData().getVerifiedGenesGFF() != null);
		boolean gtfSize = false;
		if (gtf)
			gtfSize = (!data.viewData().getVerifiedGenesGFF().isEmpty());
		boolean adapter = false;
		if (data.viewData().getCustom(ConradConstants.ADAPTED_KEY) != null) {
			adapter = (Boolean) data.viewData().getCustom(
					ConradConstants.ADAPTED_KEY);
		}
		if (!fastas || !fastasSize) {
			r.add("verified genes sequences");
		}
		if (!gtf || !gtfSize) {
			r.add("verified genes annotations");
		}
		if (!adapter)
			r.add("data adapted");
		return r;
	}

	@Override
	public void prepare(DataProxy<GFF3DataBean> data) throws Throwable {
		createFastas(data);
		createGFFs(data);
	}

	@Override
	public void update(DataProxy<GFF3DataBean> data) throws Throwable {
		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				logger.debug("using custom slot: key=" + TRAINING_FILE
						+ ", value=" + TRAINING_FILE.getAbsoluteFile());
				v.addCustom(ConradConstants.TRAINING_FILE_KEY, TRAINING_FILE.getAbsoluteFile());
			}
		});
	}

	@Override
	public boolean canBeSkipped(DataProxy<GFF3DataBean> data) throws Throwable {
		boolean trainingFile = false;
		if (getStepProperties().getProperty(ConradConstants.TRAINING_FILE_REFIX) != null) {
			trainingFile = FileUtils.fileCheck((File) data.viewData()
					.getCustom(ConradConstants.TRAINING_FILE_REFIX), false);
		}
		logger.debug("need to run: trainingFile=" + trainingFile);
		return trainingFile;
	}

	@Override
	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data)
			throws Throwable {
		// System.err.println("WENIGSTENS BIS HIER??");
		final boolean fastas = (data.viewData().getVerifiedGenesFasta() != null);
		boolean fastasSize = false;
		if (fastas) {
			fastasSize = (!data.viewData().getVerifiedGenesFasta().isEmpty());
		}
		final boolean gtf = (data.viewData().getVerifiedGenesGFF() != null);
		boolean gtfSize = false;
		if (gtf)
			gtfSize = (!data.viewData().getVerifiedGenesGFF().isEmpty());
		boolean adapter = false;
		// System.err.println("BIS HIERHER UND NICHT WEITER !!!!!!!!!!");
		if (data.viewData().getCustom(ConradConstants.ADAPTED_KEY) != null) {
			adapter = (Boolean) data.viewData().getCustom(
					ConradConstants.ADAPTED_KEY);
		}
		logger.debug(this + " requirements: fastas=" + fastas);
		logger.debug(this + " requirements: fastasSize=" + fastasSize);
		logger.debug(this + " requirements: gtf=" + gtf);
		logger.debug(this + " requirements: gtfSize=" + gtfSize);
		logger.debug(this + " requirements: data adapted=" + adapter);
		return (fastas && fastasSize && gtf && gtfSize && adapter);

	}

	@Override
	public boolean run(DataProxy<GFF3DataBean> proxy) throws Throwable {
		addShortCutFile(TRAINING_FILE);
		return start();
	}

	public boolean isCyclic() {
		return false;
	}

	// Private //

	private void createGFFs(DataProxy<GFF3DataBean> data)
			throws DataBeanAccessException, IOException {
		final File inGff = new File(ConradConstants.WORKING_DIR, "ref.gtf");
		logger.debug("ref.gtf=" + inGff);

		logger.debug("getting gtfs for veryfied genes");
		final GFF3ElementGroup gtfs = data.viewData().getVerifiedGenesGFF();

		final GFF3File gtfFile = new GFF3FileImpl(gtfs);

		logger.debug("writing gffs to " + inGff);
		gtfFile.write(inGff);

	}

	private void createFastas(DataProxy<GFF3DataBean> data)
			throws DataBeanAccessException, IOException {
		final File inFasta = new File(ConradConstants.WORKING_DIR, "ref.fasta");
		logger.debug("ref.fasta=" + inFasta);
		logger.debug("getting fastas for veryfied genes");
		final FASTAElementGroup fastas = data.viewData()
				.getVerifiedGenesFasta();
		final NewFASTAFile fastaFile = new NewFASTAFileImpl(fastas);
		logger.debug("writing fastas to " + inFasta);
		fastaFile.write(inFasta);
	}

}
