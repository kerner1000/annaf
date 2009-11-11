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
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataModifier;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

public class VerifiedGenesReader extends AbstractGFF3AnnaStep {

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

	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data) {
		logger.info(this, "no requirements needed");
		return true;
	}

	public boolean run(DataProxy<GFF3DataBean> data) throws StepExecutionException {
		try {
			doFasta(data);
			doGtf(data);
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
			// cannot be reached
			return false;
		}
		return true;
	}

	private void doGtf(DataProxy<GFF3DataBean> data) throws IOException,
			GFFFormatErrorException, DataBeanAccessException {
		logger.info(this, "reading GTF file " + gtf);
		final GFF3File gtfFile = GFF3FileImpl.convertFromGFF(gtf);
		final Collection<? extends GFF3Element> elements = gtfFile
				.getElements();
		logger.info(this, "done reading gtf");
		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				v.setVerifiedGenesGFF(new ArrayList<GFF3Element>(elements));	
			}
		});
	}

	private void doFasta(DataProxy<GFF3DataBean> data) throws IOException,
			DataBeanAccessException, StepExecutionException {
		try{
		logger.info(this, "reading FASTA file " + fasta);
		final NewFASTAFile fastaFile = NewFASTAFileImpl.parse(fasta);
		final Collection<? extends FASTAElement> sequences = fastaFile.getElements();
		logger.info(this, "done reading fasta");
		data.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				v.setVerifiedGenesFasta(new ArrayList<FASTAElement>(sequences));
			}
		});
		} catch (Throwable t) {
			StepUtils.handleException(this, t, logger);
		}
	}

	public boolean canBeSkipped(DataProxy<GFF3DataBean> data) throws StepExecutionException {
		try {
			// TODO size == 0 sub-optimal indicator
			final Collection<? extends FASTAElement> list1 = data.viewData()
					.getVerifiedGenesFasta();
			final Collection<? extends GFF3Element> list2 = data.viewData()
					.getVerifiedGenesGFF();
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
	
	public boolean isCyclic() {
		return false;
	}
}
