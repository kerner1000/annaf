package de.mpg.mpiz.koeln.anna.step.exonerate.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.file.NewGFFFile;
import de.bioutils.gff.file.NewGFFFileImpl;
import de.bioutils.gff3.converter.GFF3FileExtender;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.element.GFF3ElementBuilder;
import de.bioutils.gff3.element.GFF3ElementGroup;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.kerner.commons.StringUtils;
import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.utils.ServiceNotAvailabeException;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3WrapperStep;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataModifier;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

public abstract class AbstractStepExonerate extends AbstractGFF3WrapperStep {

	private final class DataAdapter implements GFF3FileExtender {

		public GFF3File extend(GFF3File gff3File) {
			final GFF3ElementGroup g = new GFF3ElementGroup();
			for (GFF3Element e : gff3File.getElements()) {
				final String source = e.getSource();
				final String sourceNew = source.replaceAll(":", "");
				logger.debug(this, "changing source identifier from \""
						+ source + "\" to \"" + sourceNew + "\"");
				g.add(new GFF3ElementBuilder(e).setSource(sourceNew).build());
			}
			return new GFF3FileImpl(g);
		}
	}
	
	@Override
	protected void init(BundleContext context) throws StepExecutionException {
		super.init(context);
		exeDir = new File(getStepProperties().getProperty(ExonerateConstants.EXE_DIR_KEY));
		workingDir = new File(getStepProperties().getProperty(ExonerateConstants.WORKING_DIR_KEY));
		logger.debug(this, "exeDir="+exeDir.getAbsolutePath());
		logger.debug(this, "workingDir="+workingDir.getAbsolutePath());
	}

	public void prepare(DataProxy<GFF3DataBean> data) throws Exception {
		createInputFile(data);
		createESTFasta(data);
	}

	// workingDir volatile
	private void createESTFasta(DataProxy<GFF3DataBean> data) throws DataBeanAccessException,
			ServiceNotAvailabeException, IOException {
		final Collection<FASTAElement> ests = (Collection<FASTAElement>) data
				.viewData().getESTs();
		final File f2 = new File(workingDir, ExonerateConstants.EST_FILENAME);
		new NewFASTAFileImpl(ests).write(f2);
		logger.debug(this, "created tmp file for est fasta: "
				+ f2.getAbsolutePath());
	}

	// workingDir volatile
	private void createInputFile(DataProxy<GFF3DataBean> data) throws Exception {
		final Collection<FASTAElement> inFastas = data.viewData()
				.getInputSequence();
		final File f1 = new File(workingDir, ExonerateConstants.INSEQ_FILENAME);
		new NewFASTAFileImpl(inFastas).write(f1);
		logger.debug(this, "created tmp file input sequence(s): "
				+ f1.getAbsolutePath());
	}

	// workingDir volatile
	public boolean update(DataProxy<GFF3DataBean> data) throws StepExecutionException {
		try {
			GFF3File file = GFF3FileImpl.convertFromGFF(new File(workingDir,
					ExonerateConstants.RESULT_FILENAME));
			file = new DataAdapter().extend(file);
			final Collection<? extends GFF3Element> result = file.getElements();
			data.modifiyData(new DataModifier<GFF3DataBean>() {
				public void modifiyData(GFF3DataBean v) {
					v.setMappedESTs(new ArrayList<GFF3Element>(result));
				}
			});
			return true;
		} catch (Exception e) {
			StepUtils.handleException(this, e);
			return false;
		}
	}

	public boolean run(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		logger.debug(this, "starting");
		final File file = new File(workingDir,
				ExonerateConstants.RESULT_FILENAME);		
		if (FileUtils.fileCheck(file, false) && outFileIsValid(file)) {
			super.addShortCutFile(file);
		} else {
			logger.info(this, file + " is not there or invalid, will not do shortcut");
		}
		redirectOutStreamToFile(file);
		addResultFileToWaitFor(new File(workingDir,
					ExonerateConstants.RESULT_FILENAME));
		return start();
	}

	private boolean outFileIsValid(File file) {
		NewGFFFile gff;
		try {
			gff = NewGFFFileImpl.parseFile(file);
			// TODO: size == 0 does not really indicate an invalid file
			if (gff.getElements() == null || gff.getElements().size() == 0)
				return false;
			return true;
		} catch (Exception e) {
			logger.debug(this, e.getLocalizedMessage(), e);
			return false;
		}
	}

	public boolean isCyclic() {
		return false;
	}

	public boolean canBeSkipped(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		try {
			final boolean b = (data.viewData().getMappedESTs() != null && data
					.viewData().getMappedESTs().size() != 0);
			logger.debug(this, StringUtils.getString("need to run: ests=", b));
			return b;
		} catch (Exception e) {
			StepUtils.handleException(this, e, logger);
			// cannot be reached
			return false;
		}
	}
	
	@Override
	public List<String> requirementsNeeded(DataProxy<GFF3DataBean> data)
			throws Exception {
		final List<String> r = new ArrayList<String>();
		
		final boolean ests = (data.viewData().getESTs() != null && (data
				.viewData().getESTs().size() != 0));

		final boolean inseq = (data.viewData().getInputSequence() != null && data
				.viewData().getInputSequence().size() != 0);
		
		if(!ests){
			r.add("EST sequences");
		}
		
		if(!inseq){
			r.add("input sequence(s)");
		}
		
		return r;
	}

	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		try {
			final boolean ests = (data.viewData().getESTs() != null && (data
					.viewData().getESTs().size() != 0));

			final boolean inseq = (data.viewData().getInputSequence() != null && data
					.viewData().getInputSequence().size() != 0);

			logger.debug(this, StringUtils.getString("requirements: ests=",
					ests));
			logger.debug(this, StringUtils.getString("requirements: inseq=",
					inseq));
			return (ests && inseq);
		} catch (Exception e) {
			StepUtils.handleException(this, e, logger);
			// cannot be reached
			return false;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}