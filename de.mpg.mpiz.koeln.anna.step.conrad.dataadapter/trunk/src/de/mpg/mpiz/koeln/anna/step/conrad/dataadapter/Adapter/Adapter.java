package de.mpg.mpiz.koeln.anna.step.conrad.dataadapter.Adapter;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;

import de.bioutils.DNABasicAlphabet;
import de.bioutils.fasta.FastaUtils;
import de.bioutils.fasta.NewFASTAFile;
import de.bioutils.fasta.NewFASTAFileImpl;
import de.bioutils.gff3.GFF3FASTAUnion;
import de.bioutils.gff3.GFF3FASTAUnionImpl;
import de.bioutils.gff3.GFF3Utils;
import de.bioutils.gff3.IntegrityCheckException;
import de.bioutils.gff3.Type;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.bioutils.range.Range;
import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataModifier;
import de.mpg.mpiz.koeln.anna.server.data.DataProxy;
import de.mpg.mpiz.koeln.anna.step.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.conrad.common.ConradConstants;

public class Adapter extends AbstractGFF3AnnaStep {

	private final static String OFFSET_KEY = ConradConstants.PROPERTIES_KEY_PREFIX
			+ "adapter.offset";
	private final static String MAX_LENGH_KEY = ConradConstants.PROPERTIES_KEY_PREFIX
			+ "adapter.maxElementLength";
	private final static String MAX_NUM_ELEMENTS_KEY = ConradConstants.PROPERTIES_KEY_PREFIX
			+ "adapter.maxElementNumber";
	private final static String DO_INTEGRITY_CHECK_KEY = ConradConstants.PROPERTIES_KEY_PREFIX
			+ "adapter.integrityCheck";

	private final static Log logger = new Log(Adapter.class);

	// Constructor //

	public Adapter() {
		logger.debug(this + " created");
	}

	// Private //

	private void doIntegrityCheck(GFF3FASTAUnion union)
			throws IntegrityCheckException {
		boolean integrityCheck = true;
		if (getStepProperties().getProperty(DO_INTEGRITY_CHECK_KEY) == null) {
			// nothing
		} else {
			integrityCheck = Boolean.parseBoolean(getStepProperties()
					.getProperty(DO_INTEGRITY_CHECK_KEY));
		}
		if (integrityCheck) {
			logger.debug("running union integrity test");
			union.checkIntegrity();
		}
	}

	private NewFASTAFile trimmFasta(NewFASTAFile file) {
		final String tmpHeader = file.getElements().iterator().next()
				.getHeader();
		logger.debug("trimming fasta headers");
		file = FastaUtils.trimHeader(file);
		logger.debug("done trimming fasta headers");
		logger.debug("old header: \"" + tmpHeader + "\", new header: \""
				+ file.getElements().iterator().next().getHeader() + "\"");
		return file;
	}

	private GFF3FASTAUnion trimmFastaElements(GFF3FASTAUnion union)
			throws NumberFormatException, IntegrityCheckException {
		logger.debug("trimming fasta element length (Offset:"
				+ getStepProperties().getProperty(OFFSET_KEY) + ")");
		GFF3FASTAUnion unionTrimmed = null;
		if (union.containsElementOfType(Type.gene)) {
			logger.debug("trimming by type \"" + Type.gene + "\"");
			unionTrimmed = union.trimmFastasByType(Integer
					.parseInt(getStepProperties().getProperty(OFFSET_KEY)),
					Type.gene);
		} else {
			logger.debug("trimming by attribute");
			unionTrimmed = union.trimmFastasByAttribute(Integer
					.parseInt(getStepProperties().getProperty(OFFSET_KEY)));
		}
		return unionTrimmed;
	}

	private GFF3FASTAUnion removeNonAlphabetMatching(GFF3FASTAUnion union)
			throws IntegrityCheckException {
		logger.debug("removing fastas that do not match alphabet \""
				+ new DNABasicAlphabet() + "\"");
		final GFF3FASTAUnion union2 = union
				.removeNonAlphabetMatching(new DNABasicAlphabet());
		return union2;
	}

	private GFF3FASTAUnion removeAllWithRangeGreater(GFF3FASTAUnion union)
			throws IntegrityCheckException {
		if (getStepProperties().getProperty(MAX_LENGH_KEY) == null) {
			return union;
		}
		logger.debug("discarding all elements with range >  "
				+ Integer.parseInt(getStepProperties().getProperty(
						MAX_LENGH_KEY)));
		final GFF3FASTAUnion unionReducedLength = union
				.trimmMaxElementLength(Integer.parseInt(getStepProperties()
						.getProperty(MAX_LENGH_KEY)));
		logger.debug("discarded "
				+ (union.getSize() - unionReducedLength.getSize())
				+ " elements");
		return unionReducedLength;
	}

	private GFF3FASTAUnion reduceSize(GFF3FASTAUnion union)
			throws IntegrityCheckException {
		final GFF3FASTAUnion unionReducedSize;
		if (getStepProperties().getProperty(MAX_NUM_ELEMENTS_KEY) != null) {
			logger.debug("reducing size to "
					+ Integer.parseInt(getStepProperties().getProperty(
							MAX_NUM_ELEMENTS_KEY)));
			unionReducedSize = union.reduceSize(Integer
					.parseInt(getStepProperties().getProperty(
							MAX_NUM_ELEMENTS_KEY)));
		} else {
			logger.debug("property \"" + MAX_NUM_ELEMENTS_KEY + "\" not set");
			unionReducedSize = union;
		}

		logger.debug("discarded "
				+ (union.getSize() - unionReducedSize.getSize()) + " elements");

		logger.debug("length of longest FASTA: "
				+ unionReducedSize.getFASTAElements().getLargestElement()
						.getSequence().getLength());
		logger.debug("length of longest GFF3: "
				+ unionReducedSize.getGFF3ElementGroup().getLargestElement()
						.getRange());
		return unionReducedSize;
	}

	// Override //
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected void init(BundleContext context) throws StepExecutionException {
		super.init(context);
		logger.debug(this + " init");
	}

	@Override
	public List<String> requirementsNeeded(DataProxy<GFF3DataBean> data)
			throws Throwable {
		final ArrayList<String> result = new ArrayList<String>();

		final boolean fastas = (data.viewData().getVerifiedGenesFasta() != null);
		final boolean fastasSize = (!data.viewData().getVerifiedGenesFasta()
				.isEmpty());
		final boolean gtf = (data.viewData().getVerifiedGenesGFF() != null);
		final boolean gtfSize = (!data.viewData().getVerifiedGenesGFF()
				.isEmpty());

		if (!fastas || !fastasSize) {
			result.add("verified FASTA");
		} else {
			logger.debug(this + " requirements fastas satisfied");
		}
		if (!gtf || !gtfSize) {
			result.add("verified GFF");
		} else {
			logger.debug(this + " requirements gff satisfied");
		}
		return result;
	};

	// Implement //

	@Override
	public boolean canBeSkipped(DataProxy<GFF3DataBean> proxy) throws Throwable {
		logger.debug(this + "checking need to run");
		if (proxy.viewData().getCustom(ConradConstants.ADAPTED_KEY) == null) {
			return false;
		}
		final Boolean adapted = (Boolean) proxy.viewData().getCustom(
				ConradConstants.ADAPTED_KEY);
		return adapted;
	}

	@Override
	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data)
			throws Throwable {
		final boolean fastas = (data.viewData().getVerifiedGenesFasta() != null);
		final boolean fastasSize = (!data.viewData().getVerifiedGenesFasta()
				.isEmpty());
		final boolean gtf = (data.viewData().getVerifiedGenesGFF() != null);
		final boolean gtfSize = (!data.viewData().getVerifiedGenesGFF()
				.isEmpty());
		logger.debug(this + " requirements: fastas=" + fastas);
		logger.debug(this + " requirements: fastasSize=" + fastasSize);
		logger.debug(this + " requirements: gtf=" + gtf);
		logger.debug(this + " requirements: gtfSize=" + gtfSize);
		return (fastas && fastasSize && gtf && gtfSize);
	}

	@Override
	public boolean run(DataProxy<GFF3DataBean> proxy) throws Throwable {
		boolean result = false;

		NewFASTAFile fastaFile = new NewFASTAFileImpl(proxy.viewData()
				.getVerifiedGenesFasta());

		fastaFile = trimmFasta(fastaFile);

		final GFF3File gff3File = new GFF3FileImpl(proxy.viewData()
				.getVerifiedGenesGFF());

		logger.debug("creating Union");
		GFF3FASTAUnion union = new GFF3FASTAUnionImpl(gff3File, fastaFile);
		doIntegrityCheck(union);

		final Range r = union.getGFF3ElementGroup().getRange();
		if(new Integer(getStepProperties().getProperty(MAX_LENGH_KEY)) > r.getLength()){
			logger.debug("no elements out of range, wont trimm");
		} else 
		union = trimmFastaElements(union);

		union = removeNonAlphabetMatching(union);

		union = removeAllWithRangeGreater(union);

		final GFF3FASTAUnion finalUnion = reduceSize(union);

		//logger.debug("done with adaptations");
		final int numGenes = GFF3Utils.numberOfGenes(finalUnion.getMaster());
		final double avNumExonsPerGene = GFF3Utils
				.getNumberOfExonsByGenes(finalUnion.getMaster().getElements());
		logger.info("adaptaions done, using " + numGenes + " genes, with "
				+ String.format("%2.2f", avNumExonsPerGene)
				+ " average exons per gene");
		doIntegrityCheck(finalUnion);

		proxy.modifiyData(new DataModifier<GFF3DataBean>() {
			public void modifiyData(GFF3DataBean v) {
				logger.debug("modifying data");
				v.setVerifiedGenesFasta(finalUnion.getFASTAElements());
				v.setVerifiedGenesGFF(finalUnion.getGFF3ElementGroup());
				v.addCustom(ConradConstants.ADAPTED_KEY, Boolean.TRUE);
			}
		});

		logger.debug("data adaptation sucessfull");
		result = true;
		return result;
	}

	public boolean isCyclic() {
		return false;
	}

}
