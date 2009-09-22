package de.mpg.mpiz.koeln.anna.server.data.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.gff.element.NewGFFElement;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;

public class GFF3DataBeanImpl implements GFF3DataBean {
	
	private static final long serialVersionUID = -8241571899555002582L;
	private ArrayList<FASTAElement> inputSequence = new ArrayList<FASTAElement>();
	private ArrayList<FASTAElement> verifiedGenesFasta = new ArrayList<FASTAElement>();
	private ArrayList<NewGFFElement> verifiedGenesGFF = new ArrayList<NewGFFElement>();
	private ArrayList<NewGFFElement> predictedGenesGFF = new ArrayList<NewGFFElement>();
	private ArrayList<NewGFFElement> repeatMaskerGFF = new ArrayList<NewGFFElement>();
	private Map<String, Serializable> custom = new HashMap<String, Serializable>();
	
	public Collection<FASTAElement> getInputSequence() {
		return inputSequence;
	}
	public void setInputSequence(Collection<FASTAElement> inputSequence) {
		this.inputSequence.clear();
		this.inputSequence.addAll(inputSequence);
	}
	public Collection<FASTAElement> getVerifiedGenesFasta() {
		return verifiedGenesFasta;
	}
	public void setVerifiedGenesFasta(Collection<FASTAElement> verifiedGenesFasta) {
		this.verifiedGenesFasta.clear();
		this.verifiedGenesFasta.addAll(verifiedGenesFasta);
	}
	public Collection<NewGFFElement> getVerifiedGenesGFF() {
		return verifiedGenesGFF;
	}
	public void setVerifiedGenesGFF(Collection<NewGFFElement> verifiedGenesGFF) {
		this.verifiedGenesGFF.clear();
		this.verifiedGenesGFF.addAll(verifiedGenesGFF);
	}
	public Collection<NewGFFElement> getPredictedGenesGFF() {
		return predictedGenesGFF;
	}
	public void setPredictedGenesGFF(Collection<NewGFFElement> predictedGenesGFF) {
		this.predictedGenesGFF.clear();
		this.predictedGenesGFF.addAll(predictedGenesGFF);
	}
	public Collection<NewGFFElement> getRepeatMaskerGFF() {
		return repeatMaskerGFF;
	}
	public void setRepeatMaskerGFF(Collection<NewGFFElement> repeatMaskerGFF) {
		this.repeatMaskerGFF.clear();
		this.repeatMaskerGFF.addAll(repeatMaskerGFF);
	}
	public Map<String, Serializable> getCustom() {
		return custom;
	}
	public void setCustom(Map<String, Serializable> custom) {
		this.custom = custom;
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}
}
