package de.mpg.mpiz.koeln.anna.step.verifiedgenes.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.osgi.framework.BundleContext;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff.element.NewGFFElement;
import de.bioutils.gff.file.NewGFFFile;
import de.bioutils.gff.file.NewGFFFileImpl;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcherImpl;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.AbstractStep;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

public class VerifiedGenesReader extends AbstractStep {

	private final static String FASTA_KEY = "anna.step.verified.fasta";
	private final static String GTF_KEY = "anna.step.verified.gtf";

	private File fasta;
	private File gtf;
	private LogDispatcher logger = null;

	public VerifiedGenesReader() {
		// use "init()" instead, to make sure "logger" is initiated
	}

	@Override
	protected synchronized void init(BundleContext context)
			throws StepExecutionException {
		super.init(context);
		logger = new LogDispatcherImpl(context);
		initFiles();
	}

	private void initFiles() {
		final String fastaPath = super.getStepProperties().getProperty(
				FASTA_KEY);
		logger.debug(this, "got path for FASTA: " + fastaPath);
		final String gtfPath = super.getStepProperties().getProperty(GTF_KEY);
		logger.debug(this, "got path for GTF: " + gtfPath);
		fasta = new File(fastaPath);
		gtf = new File(gtfPath);
	}

	public boolean requirementsSatisfied(DataProxy data) {
		logger.info(this, "no requirements needed");
		return true;
	}

	public boolean run(DataProxy data, StepProcessObserver observer) throws StepExecutionException {
		observer.setProgress(0, 100);
		try {
			observer.setProgress(30, 100);
			doFasta(data);
			observer.setProgress(60, 100);
			doGtf(data);
			observer.setProgress(100, 100);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
		return true;
	}

	private void doGtf(DataProxy data) throws IOException,
			GFFFormatErrorException, DataBeanAccessException {
		logger.info(this, "reading GTF file " + gtf);
		final NewGFFFile gtfFile = NewGFFFileImpl.parseFile(gtf);
		final Collection<? extends NewGFFElement> elements = gtfFile
				.getElements();
		logger.info(this, "done reading gtf");
		data.setVerifiedGenesGff(new ArrayList<NewGFFElement>(elements));
	}

	private void doFasta(DataProxy data) throws IOException,
			DataBeanAccessException, StepExecutionException {
		try{
		logger.info(this, "reading FASTA file " + fasta);
		final NewFASTAFile fastaFile = NewFASTAFileImpl.parse(fasta);
		final Collection<? extends FASTAElement> sequences = fastaFile.getElements();
		logger.info(this, "done reading fasta");
		data.setVerifiedGenesFasta(new ArrayList<FASTAElement>(sequences));
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
		}
	}

	public boolean canBeSkipped(DataProxy data) throws StepExecutionException {
		try {
			// TODO size == 0 sub-optimal indicator
			final Collection<? extends FASTAElement> list1 = data
					.getVerifiedGenesFasta();
			final Collection<? extends NewGFFElement> list2 = data
					.getVerifiedGenesGff();
			return (list1 != null && list1.size() != 0 && list2 != null && list2
					.size() != 0);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
