package de.mpg.mpiz.koeln.anna.server.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.gff.element.NewGFFElement;

public interface GFF3DataBean extends DataBean{
	
	public ArrayList<FASTAElement> getInputSequence();
	
	public void setInputSequence(ArrayList<FASTAElement> inputSequence);
	
	public ArrayList<FASTAElement> getVerifiedGenesFasta();
	
	public void setVerifiedGenesFasta(ArrayList<FASTAElement> verifiedGenesFasta);
	
	public ArrayList<NewGFFElement> getVerifiedGenesGFF();
	
	public void setVerifiedGenesGFF(ArrayList<NewGFFElement> verifiedGenesGFF);
	
	public ArrayList<NewGFFElement> getPredictedGenesGFF();
	
	public void setPredictedGenesGFF(ArrayList<NewGFFElement> predictedGenesGFF);
	
	public ArrayList<NewGFFElement> getRepeatMaskerGFF();
	
	public void setRepeatMaskerGFF(ArrayList<NewGFFElement> repeatMaskerGFF);
	
	public Map<String, Serializable> getCustom();
	
	public void setCustom(Map<String, Serializable> custom);

}
