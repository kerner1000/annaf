package de.mpg.mpiz.koeln.anna.server.data.impl;

import java.io.Serializable;
import java.util.ArrayList;
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
	
	public ArrayList<FASTAElement> getInputSequence() {
		return inputSequence;
	}
	public void setInputSequence(ArrayList<FASTAElement> inputSequence) {
		this.inputSequence = inputSequence;
	}
	public ArrayList<FASTAElement> getVerifiedGenesFasta() {
		return verifiedGenesFasta;
	}
	public void setVerifiedGenesFasta(ArrayList<FASTAElement> verifiedGenesFasta) {
		this.verifiedGenesFasta = verifiedGenesFasta;
	}
	public ArrayList<NewGFFElement> getVerifiedGenesGFF() {
		return verifiedGenesGFF;
	}
	public void setVerifiedGenesGFF(ArrayList<NewGFFElement> verifiedGenesGFF) {
		this.verifiedGenesGFF = verifiedGenesGFF;
	}
	public ArrayList<NewGFFElement> getPredictedGenesGFF() {
		return predictedGenesGFF;
	}
	public void setPredictedGenesGFF(ArrayList<NewGFFElement> predictedGenesGFF) {
		this.predictedGenesGFF = predictedGenesGFF;
	}
	public ArrayList<NewGFFElement> getRepeatMaskerGFF() {
		return repeatMaskerGFF;
	}
	public void setRepeatMaskerGFF(ArrayList<NewGFFElement> repeatMaskerGFF) {
		this.repeatMaskerGFF = repeatMaskerGFF;
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
