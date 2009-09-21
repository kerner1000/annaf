package de.mpg.mpiz.koeln.anna.step.inputsequencereader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataModifier;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

public class InputSequenceReader extends AbstractGFF3AnnaStep {

	private final static String INFILE_KEY = "anna.step.inputsequencereader.infile";

	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		logger.debug(this, "no requirements needed");
		return true;
	}

	public boolean canBeSkipped(DataProxy<GFF3DataBean> data) throws StepExecutionException {
		try {
			final boolean inputSequences = (data.viewData().getInputSequence() != null);
			final boolean inputSequencesSize = (data.viewData().getInputSequence().size() != 0);
			logger.debug(this, "need to run:");
			logger.debug(this, "\tinputSequences=" + inputSequences);
			logger.debug(this, "\tinputSequencesSize=" + inputSequencesSize);
			return (inputSequences && inputSequencesSize);
		} catch (Exception e) {
			StepUtils.handleException(this, e, logger);
			// cannot be reached
			return false;
		}
	}

	public boolean run(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		try {
			final File inFile = new File(getStepProperties().getProperty(
					INFILE_KEY));
			logger.debug(this, "reading file " + inFile);
			final Collection<? extends FASTAElement> fastas = NewFASTAFileImpl.parse(inFile).getElements();
			if (fastas == null || fastas.size() == 0) {
				logger.warn(this, "file " + inFile + " is invalid");
				return false;
			}
			logger.debug(this, "got input sequences:"
					+ fastas.iterator().next().getHeader() + " [...]");
			data.modifiyData(new DataModifier<GFF3DataBean>() {
				public void modifiyData(GFF3DataBean v) {
					v.setInputSequence(new ArrayList<FASTAElement>(fastas));
				}
			});
			return true;
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
