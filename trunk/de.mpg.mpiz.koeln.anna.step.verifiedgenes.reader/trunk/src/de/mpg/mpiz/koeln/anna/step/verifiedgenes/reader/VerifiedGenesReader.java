package de.mpg.mpiz.koeln.anna.step.verifiedgenes.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.BundleContext;

import de.bioutils.AbstractSequence;
import de.bioutils.DNABasicAlphabet;
import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.FASTAElementGroup;
import de.bioutils.fasta.FASTAElementGroupImpl;
import de.bioutils.fasta.FASTAElementImpl;
import de.bioutils.fasta.FastaUtils;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff3.GFF3Utils;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.element.GFF3ElementGroup;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataModifier;
import de.mpg.mpiz.koeln.anna.server.data.DataProxy;
import de.mpg.mpiz.koeln.anna.step.StepExecutionException;

public class VerifiedGenesReader extends AbstractGFF3AnnaStep {

	private final static String FASTA_KEY = "anna.step.verified.fasta";
	private final static String GTF_KEY = "anna.step.verified.gtf";

	private File fasta;
	private File gtf;

	public VerifiedGenesReader() {
		// use "init()" instead
	}

	@Override
	protected synchronized void init(BundleContext context)
			throws StepExecutionException {
		super.init(context);
		initFiles();
	}

	private void initFiles() throws StepExecutionException {
		final String fastaPath = super.getStepProperties().getProperty(
				FASTA_KEY);
		logger.debug("got path for FASTA: " + fastaPath);
		final String gtfPath = super.getStepProperties().getProperty(GTF_KEY);
		logger.debug("got path for GTF: " + gtfPath);
		if(fastaPath == null || gtfPath == null)
			throw new StepExecutionException(this, "could not determine path to file(s), please check settings");
		fasta = new File(fastaPath);
		gtf = new File(gtfPath);
		if(!FileUtils.fileCheck(fasta, false)){
			throw new StepExecutionException(this, "cannot access file \"" + fasta + "\", step settings correct?");
		}
		if(!FileUtils.fileCheck(gtf, false)){
			throw new StepExecutionException(this, "cannot access file \"" + gtf + "\", step settings correct?");
		}
	}

	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data) {
		logger.info("no requirements needed");
		return true;
	}

	public boolean run(DataProxy<GFF3DataBean> data) throws Throwable {
		doFasta(data);
		doGtf(data);
		return true;
	}

	private void doGtf(DataProxy<GFF3DataBean> data) throws IOException,
			GFFFormatErrorException, DataBeanAccessException {
		logger.info("reading GFF file " + gtf);
		// TODO file may not be sorted.
		final GFF3File gtfFile = GFF3Utils.convertFromGFFFile(gtf, true);
		final GFF3ElementGroup elements = gtfFile.getElements();
		
		final GFF3ElementGroup elements2 = trimmGFF(elements);
		
		if(elements.getSize() != elements2.getSize()){
			logger.warn("size of elements before trimm: " + elements.getSize() + ", size of elements after trimm: " + elements2.getSize());
		}
		
		logger.info("done reading GFF");
		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				v.setVerifiedGenesGFF(elements2);
			}
		});
	}

	private GFF3ElementGroup trimmGFF(GFF3ElementGroup elements) {
		final String tmpIdent = elements.iterator().next().getSeqID();
		logger.debug("trimming gff3 sequence identifiers");
		elements = GFF3Utils.trimHeader(elements);
		logger.debug("done trimming gff3 sequence identifiers");
		logger.debug("old ident: \"" + tmpIdent + "\", new ident: \""
				+ elements.iterator().next().getSeqID() + "\"");
		return elements;
	}

	private void doFasta(DataProxy<GFF3DataBean> data) throws IOException,
			DataBeanAccessException, StepExecutionException {
		logger.info("reading FASTA file " + fasta);
		final NewFASTAFile fastaFile = NewFASTAFileImpl.parse(fasta);
		FASTAElementGroup sequences = fastaFile.getElements();
		logger.info("done reading fasta");
		
		sequences = trimmFasta(sequences);
		
		logger.debug("checking for valid alphabet");
		final FASTAElementGroup sequencesNew = FastaUtils.adaptToAlphabet(sequences, new DNABasicAlphabet());
		
		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				v.setVerifiedGenesFasta(sequencesNew);
			}
		});
	}
	
	private FASTAElementGroup trimmFasta(FASTAElementGroup fastas) {
		final String tmpHeader = fastas.iterator().next()
				.getHeader();
		logger.debug("trimming fasta headers");
		fastas = FastaUtils.trimHeader(fastas);
		logger.debug("done trimming fasta headers");
		logger.debug("old header: \"" + tmpHeader + "\", new header: \""
				+ fastas.iterator().next().getHeader() + "\"");
		return fastas;
	}

	public boolean canBeSkipped(DataProxy<GFF3DataBean> data) throws Throwable {
		// TODO size == 0 sub-optimal indicator
		final FASTAElementGroup list1 = data.viewData().getVerifiedGenesFasta();
		final GFF3ElementGroup list2 = data.viewData().getVerifiedGenesGFF();
		return (list1 != null && !list1.isEmpty() && list2 != null && !list2
				.isEmpty());
	}

	@Override
	public List<String> requirementsNeeded(DataProxy<GFF3DataBean> data) throws Throwable {
		final ArrayList<String> result = new ArrayList<String>();
		final FASTAElementGroup list1 = data.viewData().getVerifiedGenesFasta();
		final GFF3ElementGroup list2 = data.viewData().getVerifiedGenesGFF();
		if(list1 == null || list1.isEmpty())
			result.add("verified genes fasta");
		if(list2 == null || list2.isEmpty())
			result.add("verified genes GFF");
		return result;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	public boolean isCyclic() {
		return false;
	}
}
