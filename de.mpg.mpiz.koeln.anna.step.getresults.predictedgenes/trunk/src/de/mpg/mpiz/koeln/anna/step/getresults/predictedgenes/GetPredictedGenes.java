package de.mpg.mpiz.koeln.anna.step.getresults.predictedgenes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.bioutils.gff.element.NewGFFElement;
import de.bioutils.gff.file.NewGFFFile;
import de.bioutils.gff.file.NewGFFFileImpl;
import de.mpg.mpiz.koeln.anna.abstractstep.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.server.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

public class GetPredictedGenes extends AbstractGFF3AnnaStep {

	private final static String OUT_DIR_KEY = "anna.step.getResults.outDir";
	private final static String OUT_FILE_NAME_KEY = "anna.step.getResults.predictedGenes.fileName";

	public boolean requirementsSatisfied(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		try {
			final Collection<? extends NewGFFElement> elements = data.viewData()
					.getPredictedGenesGFF();
			// TODO predicted genes may be size==0
			return (elements != null && elements.size() != 0);
		} catch (Throwable t) {
			StepUtils.handleException(this, t);
			// cannot be reached
			return false;
		}
	}

	public boolean canBeSkipped(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		return false;
	}

	public boolean run(DataProxy<GFF3DataBean> data)
			throws StepExecutionException {
		boolean success = false;
		try {
			final File outDir = new File(super.getStepProperties().getProperty(
					OUT_DIR_KEY));
			success = checkOutDir(outDir);
			if (success) {
				writeAllToOne(outDir, data);
				writeAllToSeparateFile(outDir, data);
			}
		} catch (Throwable t) {
			StepUtils.handleException(this, t);
			// cannot be reached
			return false;
		}
		return success;
	}

	private void writeAllToSeparateFile(File outDir, DataProxy<GFF3DataBean> data) throws DataBeanAccessException, IOException {
		Collection<? extends NewGFFElement> ele = data.viewData().getPredictedGenesGFF();
		final NewGFFFile file = new NewGFFFileImpl(ele);
		Map<String, List<NewGFFElement>> set = splitToSeqNames(file);
		for(Entry<String, List<NewGFFElement>> e : set.entrySet()){
			String fileName = super.getStepProperties()
			.getProperty(OUT_FILE_NAME_KEY);
			final int dot = fileName.lastIndexOf(".");
			final String s1 = fileName.substring(0, dot);
			final String s2 = fileName.substring(dot);
			fileName = s1 + "_" + e.getKey() + s2;
			final File outFile = new File(outDir, fileName);
			logger.info(this, ": writing predicted genes to "
					+ outFile);
			final NewGFFFile file2 = new NewGFFFileImpl(e.getValue());
			file2.write(outFile);
		}
		
	}

	private static Map<String, List<NewGFFElement>> splitToSeqNames(NewGFFFile file) {
		final Map<String, List<NewGFFElement>> map = new HashMap<String, List<NewGFFElement>>();
		for(NewGFFElement e : file.getElements()){
			final String id = e.getSeqName();
			if(map.containsKey(id)){
				map.get(id).add(e);
			} else {
				final List<NewGFFElement> l = new ArrayList<NewGFFElement>();
				l.add(e);
				map.put(id, l);
			}
		}
		return map;
	}

	private void writeAllToOne(File outDir, DataProxy<GFF3DataBean> data) throws DataBeanAccessException, IOException {
		final File outFile = new File(outDir, super.getStepProperties()
				.getProperty(OUT_FILE_NAME_KEY));
		logger.info(this, ": writing predicted genes to "
				+ outFile);
		final NewGFFFile file = new NewGFFFileImpl(data.viewData()
				.getPredictedGenesGFF());
		file.write(outFile);	
	}

	private boolean checkOutDir(File outFile) {
		if (!outFile.exists()) {
			logger.info(this, outFile
					+ " does not exist, creating");
			final boolean b = outFile.mkdirs();
			return b;
		}
		return outFile.canWrite();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	public boolean isCyclic() {
		return false;
	}
}
