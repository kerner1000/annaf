package de.mpg.mpiz.koeln.anna.server.data.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.gff3.element.GFF3Element;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;

public class GFF3DataBeanImpl implements GFF3DataBean {
	
	private static final long serialVersionUID = -8241571899555002582L;
	private ArrayList<FASTAElement> inputSequence = new ArrayList<FASTAElement>();
	private ArrayList<FASTAElement> ests = new ArrayList<FASTAElement>();
	private ArrayList<FASTAElement> verifiedGenesFasta = new ArrayList<FASTAElement>();
	private ArrayList<GFF3Element> verifiedGenesGFF = new ArrayList<GFF3Element>();
	private ArrayList<GFF3Element> predictedGenesGFF = new ArrayList<GFF3Element>();
	private ArrayList<GFF3Element> repeatMaskerGFF = new ArrayList<GFF3Element>();
	private ArrayList<GFF3Element> mappedESTs = new ArrayList<GFF3Element>();
	
	private Map<String, Serializable> custom = new ConcurrentHashMap<String, Serializable>();
	
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
	public Collection<GFF3Element> getVerifiedGenesGFF() {
		return verifiedGenesGFF;
	}
	public void setVerifiedGenesGFF(Collection<GFF3Element> verifiedGenesGFF) {
		this.verifiedGenesGFF.clear();
		this.verifiedGenesGFF.addAll(verifiedGenesGFF);
	}
	public Collection<GFF3Element> getPredictedGenesGFF() {
		return predictedGenesGFF;
	}
	public void setPredictedGenesGFF(Collection<GFF3Element> predictedGenesGFF) {
		this.predictedGenesGFF.clear();
		this.predictedGenesGFF.addAll(predictedGenesGFF);
	}
	public Collection<GFF3Element> getRepeatMaskerGFF() {
		return repeatMaskerGFF;
	}
	public void setRepeatMaskerGFF(Collection<GFF3Element> repeatMaskerGFF) {
		this.repeatMaskerGFF.clear();
		this.repeatMaskerGFF.addAll(repeatMaskerGFF);
	}
	public Serializable getCustom(String ident) {
		return custom.get(ident);
	}
	public void addCustom(String ident, Serializable custom) {
		this.custom.put(ident, custom);
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}
	public Collection<FASTAElement> getESTs() {
		return ests;
	}
	public void setESTs(Collection<FASTAElement> ests) {
		this.ests.clear();
		this.ests.addAll(ests);
		
	}
	public Collection<GFF3Element> getMappedESTs() {
		return mappedESTs;
	}
	public void setMappedESTs(Collection<GFF3Element> mappedESTs) {
		this.mappedESTs.clear();
		this.mappedESTs.addAll(mappedESTs);
	}
}
