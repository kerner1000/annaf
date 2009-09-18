package de.mpg.mpiz.koeln.anna.server.dataproxy;

import java.io.File;
import java.util.ArrayList;

import de.bioutils.fasta.FASTAElement;
import de.bioutils.gff.element.NewGFFElement;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;

public interface DataProxy {

	void setVerifiedGenesGff(ArrayList<? extends NewGFFElement> elements)
			throws DataBeanAccessException;

	void setVerifiedGenesFasta(ArrayList<? extends FASTAElement> sequences)
			throws DataBeanAccessException;

	ArrayList<? extends FASTAElement> getVerifiedGenesFasta()
			throws DataBeanAccessException;

	ArrayList<? extends NewGFFElement> getVerifiedGenesGff()
			throws DataBeanAccessException;

	ArrayList<? extends FASTAElement> getInputSequences()
			throws DataBeanAccessException;

	void setInputSequences(ArrayList<? extends FASTAElement> fastas)
			throws DataBeanAccessException;

	File getConradTrainingFile() throws DataBeanAccessException;

	void setConradTrainingFile(File trainingFile)
			throws DataBeanAccessException;

	ArrayList<? extends NewGFFElement> getPredictedGenesGtf()
			throws DataBeanAccessException;

	void setPredictedGenesGff(ArrayList<? extends NewGFFElement> result)
			throws DataBeanAccessException;

	ArrayList<? extends NewGFFElement> getRepeatMaskerGff()
			throws DataBeanAccessException;

	void setRepeatMaskerGff(ArrayList<? extends NewGFFElement> elements)
			throws DataBeanAccessException;

}
