package de.mpg.mpiz.koeln.anna.step.inputsequencereader;

import java.io.File;

import de.bioutils.fasta.FASTAElementGroup;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataModifier;
import de.mpg.mpiz.koeln.anna.server.data.DataProxy;

public class InputSequenceReader extends AbstractGFF3AnnaStep {

	private final static String INFILE_KEY = "anna.step.inputsequencereader.infile";

	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data)
			throws Throwable {
		logger.debug("no requirements needed");
		return true;
	}

	public boolean canBeSkipped(DataProxy<GFF3DataBean> data) throws Throwable {

		final boolean inputSequences = (data.viewData().getInputSequence() != null);
		final boolean inputSequencesSize = (!data.viewData().getInputSequence()
				.isEmpty());
		logger.debug("need to run:");
		logger.debug("\tinputSequences=" + inputSequences);
		logger.debug("\tinputSequencesSize=" + inputSequencesSize);
		return (inputSequences && inputSequencesSize);
	}

	public boolean run(DataProxy<GFF3DataBean> data) throws Throwable {

		final File inFile = new File(getStepProperties()
				.getProperty(INFILE_KEY));
		logger.info("reading file " + inFile);
		final FASTAElementGroup fastas = NewFASTAFileImpl.parse(inFile)
				.getElements();
		if (fastas == null || fastas.isEmpty()) {
			logger.warn("file " + inFile + " is invalid");
			return false;
		}
		logger.debug("got input sequences:"
				+ fastas.iterator().next().getHeader() + " [...]");
		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				v.setInputSequence(fastas);
			}
		});
		return true;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public boolean isCyclic() {
		return false;
	}

}
