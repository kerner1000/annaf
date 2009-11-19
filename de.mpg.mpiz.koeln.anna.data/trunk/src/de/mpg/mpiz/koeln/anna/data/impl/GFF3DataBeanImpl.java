package de.mpg.mpiz.koeln.anna.data.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.bioutils.fasta.FASTAElementGroup;
import de.bioutils.fasta.FASTAElementGroupImpl;
import de.bioutils.gff3.element.GFF3ElementGroup;
import de.bioutils.gff3.element.GFF3ElementGroupImpl;
import de.mpg.mpiz.koeln.anna.data.GFF3DataBean;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-11-12
 *
 */
public class GFF3DataBeanImpl implements GFF3DataBean {
	
	private static final long serialVersionUID = -8241571899555002582L;
	private FASTAElementGroup inputSequence = new FASTAElementGroupImpl();
	private FASTAElementGroup ests = new FASTAElementGroupImpl();
	private FASTAElementGroup verifiedGenesFasta = new FASTAElementGroupImpl();
	
	//TODO gff3 files must be sorted!!
	private GFF3ElementGroup verifiedGenesGFF = new GFF3ElementGroupImpl(true);
	private GFF3ElementGroup predictedGenesGFF = new GFF3ElementGroupImpl(true);
	private GFF3ElementGroup repeatMaskerGFF = new GFF3ElementGroupImpl(true);
	private GFF3ElementGroup mappedESTs = new GFF3ElementGroupImpl(true);
	
	private Map<String, Serializable> custom = new ConcurrentHashMap<String, Serializable>();
	
	public FASTAElementGroup getInputSequence() {
		return inputSequence;
	}
	public void setInputSequence(FASTAElementGroup inputSequence) {
		this.inputSequence.clear();
		this.inputSequence.addAll(inputSequence);
	}
	public FASTAElementGroup getVerifiedGenesFasta() {
		return verifiedGenesFasta;
	}
	public void setVerifiedGenesFasta(FASTAElementGroup verifiedGenesFasta) {
		this.verifiedGenesFasta.clear();
		this.verifiedGenesFasta.addAll(verifiedGenesFasta);
	}
	public GFF3ElementGroup getVerifiedGenesGFF() {
		return verifiedGenesGFF;
	}
	public void setVerifiedGenesGFF(GFF3ElementGroup verifiedGenesGFF) {
		this.verifiedGenesGFF.clear();
		this.verifiedGenesGFF.addAll(verifiedGenesGFF);
	}
	public GFF3ElementGroup getPredictedGenesGFF() {
		return predictedGenesGFF;
	}
	public void setPredictedGenesGFF(GFF3ElementGroup predictedGenesGFF) {
		this.predictedGenesGFF.clear();
		this.predictedGenesGFF.addAll(predictedGenesGFF);
	}
	public GFF3ElementGroup getRepeatMaskerGFF() {
		return repeatMaskerGFF;
	}
	public void setRepeatMaskerGFF(GFF3ElementGroup repeatMaskerGFF) {
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
	public FASTAElementGroup getESTs() {
		return ests;
	}
	public void setESTs(FASTAElementGroup ests) {
		this.ests.clear();
		this.ests.addAll(ests);
		
	}
	public GFF3ElementGroup getMappedESTs() {
		return mappedESTs;
	}
	public void setMappedESTs(GFF3ElementGroup mappedESTs) {
		this.mappedESTs.clear();
		this.mappedESTs.addAll(mappedESTs);
	}
}
