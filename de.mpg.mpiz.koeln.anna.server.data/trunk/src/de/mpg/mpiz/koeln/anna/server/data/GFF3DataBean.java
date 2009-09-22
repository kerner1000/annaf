package de.mpg.mpiz.koeln.anna.server.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.gff.element.NewGFFElement;

public interface GFF3DataBean extends DataBean {
	
	public Collection<FASTAElement> getInputSequence();
	
	public void setInputSequence(Collection<FASTAElement> inputSequence);
	
	public Collection<FASTAElement> getVerifiedGenesFasta();
	
	public void setVerifiedGenesFasta(Collection<FASTAElement> verifiedGenesFasta);
	
	public Collection<NewGFFElement> getVerifiedGenesGFF();
	
	public void setVerifiedGenesGFF(Collection<NewGFFElement> verifiedGenesGFF);
	
	public Collection<NewGFFElement> getPredictedGenesGFF();
	
	public void setPredictedGenesGFF(Collection<NewGFFElement> predictedGenesGFF);
	
	public Collection<NewGFFElement> getRepeatMaskerGFF();
	
	public void setRepeatMaskerGFF(Collection<NewGFFElement> repeatMaskerGFF);
	
	public Map<String, Serializable> getCustom();
	
	public void setCustom(Map<String, Serializable> custom);

}
