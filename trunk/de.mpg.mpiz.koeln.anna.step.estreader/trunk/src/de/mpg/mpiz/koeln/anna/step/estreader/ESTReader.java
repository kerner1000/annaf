package de.mpg.mpiz.koeln.anna.step.estreader;

import java.io.File;

import de.bioutils.DNABasicAlphabet;
import de.bioutils.fasta.FASTAElementGroup;
import de.bioutils.fasta.FASTAElementGroupImpl;
import de.bioutils.fasta.FastaUtils;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataModifier;
import de.mpg.mpiz.koeln.anna.server.data.DataProxy;
import de.mpg.mpiz.koeln.anna.step.StepExecutionException;

public class ESTReader extends AbstractGFF3AnnaStep {

	private final static String INFILE_KEY = "anna.step.estreader.infile";

	@Override
	public boolean canBeSkipped(DataProxy<GFF3DataBean> proxy) throws Throwable {
		boolean b = true;
		final FASTAElementGroup c = proxy.viewData().getESTs();
		b = (c != null && c.asList().size() != 0);
		logger.debug("can be skipped=" + b + "(" + c.asList().size()
				+ ") est fastas retrieved");
		return b;
	}

	@Override
	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> proxy)
			throws StepExecutionException {
		return true;
	}

	@Override
	public boolean run(DataProxy<GFF3DataBean> proxy) throws Throwable {
		final File file = new File(getStepProperties().getProperty(INFILE_KEY));
		logger.debug("reading file " + file);
		NewFASTAFile f = NewFASTAFileImpl.parse(file);
		logger.debug("reading file " + file + " done, updating data");

		f = trimmFasta(f);

		logger.debug("checking for valid alphabet");
		final FASTAElementGroup sequencesNew = FastaUtils.adaptToAlphabet(
				f.getElements(), new DNABasicAlphabet());

		proxy.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				v.setESTs(new FASTAElementGroupImpl(sequencesNew));
				logger.debug("updating data done");
			}
		});
		return true;
	}

	private NewFASTAFile trimmFasta(NewFASTAFile fastas) {
		final String tmpHeader = fastas.getElements().iterator().next().getHeader();
		logger.debug("trimming fasta headers");
		fastas = FastaUtils.trimHeader(fastas);
		logger.debug("done trimming fasta headers");
		logger.debug("old header: \"" + tmpHeader + "\", new header: \""
				+ fastas.getElements().iterator().next().getHeader() + "\"");
		return fastas;
	}

	public boolean isCyclic() {
		return false;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
