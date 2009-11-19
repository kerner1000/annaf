package de.mpg.mpiz.koeln.anna.data;

import de.bioutils.fasta.FASTAElementGroup;
import de.bioutils.gff3.element.GFF3ElementGroup;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-11-12
 *
 */
public interface GFF3DataBean extends DataBean {
	
	public FASTAElementGroup getInputSequence();
	
	public void setInputSequence(FASTAElementGroup inputSequence);
	
	public FASTAElementGroup getVerifiedGenesFasta();
	
	public void setVerifiedGenesFasta(FASTAElementGroup verifiedGenesFasta);
	
	public GFF3ElementGroup getVerifiedGenesGFF();
	
	public void setVerifiedGenesGFF(GFF3ElementGroup verifiedGenesGFF);
	
	public GFF3ElementGroup getPredictedGenesGFF();
	
	public void setPredictedGenesGFF(GFF3ElementGroup predictedGenesGFF);
	
	public GFF3ElementGroup getRepeatMaskerGFF();
	
	public void setRepeatMaskerGFF(GFF3ElementGroup repeatMaskerGFF);
	
	public FASTAElementGroup getESTs();
	
	public void setESTs(FASTAElementGroup ests);
	
	public GFF3ElementGroup getMappedESTs();
	
	public void setMappedESTs(GFF3ElementGroup mappedESTs);

}
