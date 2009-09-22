package de.mpg.mpiz.koeln.anna.server.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.gff3.element.GFF3Element;

public interface GFF3DataBean extends DataBean {
	
	public Collection<FASTAElement> getInputSequence();
	
	public void setInputSequence(Collection<FASTAElement> inputSequence);
	
	public Collection<FASTAElement> getVerifiedGenesFasta();
	
	public void setVerifiedGenesFasta(Collection<FASTAElement> verifiedGenesFasta);
	
	public Collection<GFF3Element> getVerifiedGenesGFF();
	
	public void setVerifiedGenesGFF(Collection<GFF3Element> verifiedGenesGFF);
	
	public Collection<GFF3Element> getPredictedGenesGFF();
	
	public void setPredictedGenesGFF(Collection<GFF3Element> predictedGenesGFF);
	
	public Collection<GFF3Element> getRepeatMaskerGFF();
	
	public void setRepeatMaskerGFF(Collection<GFF3Element> repeatMaskerGFF);
	
	public Map<String, Serializable> getCustom();
	
	public void setCustom(Map<String, Serializable> custom);

}
