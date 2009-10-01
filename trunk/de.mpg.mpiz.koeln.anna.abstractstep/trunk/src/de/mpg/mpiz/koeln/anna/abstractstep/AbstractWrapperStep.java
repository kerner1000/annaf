package de.mpg.mpiz.koeln.anna.abstractstep;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;

public abstract class AbstractWrapperStep<T> extends AbstractAnnaStep<T> {

	protected volatile File exeDir;
	protected volatile File workingDir;
	protected List<File> shortCutFiles = new ArrayList<File>();
	private File outFile = null;

	/**
	 * <p>
	 * Preparation for running wrapped process. (e.g. creating required files in
	 * working directory)
	 * </p>
	 * 
	 * @throws Exception
	 */
	public abstract void prepare() throws Exception;

	/**
	 * @return List of command line arguments, that will passed to wrapped step.
	 */
	public abstract List<String> getCmdList();

	/**
	 * <p>
	 * Finally start this Step.
	 * </p>
	 * 
	 * @throws StepExecutionException
	 */
	public boolean start() throws StepExecutionException {
		boolean success = false;
		try {
			printProperties();
			validateProperties();
			prepare();
			final AbstractStepProcessBuilder p = new AbstractStepProcessBuilder(
					exeDir, workingDir, logger) {
				@Override
				protected List<String> getCommandList() {
					return getCmdList();
				}
			};
			final Map<File, Boolean> tmpMap = new HashMap<File, Boolean>();
			for (File f : shortCutFiles) {
				tmpMap.put(f, Boolean.TRUE);
			}
			p.addAllResultFiles(tmpMap);
			
			OutputStream out = System.out;
			OutputStream err = System.err;
			if(outFile != null){
				out = FileUtils.getOutputStreamForFile(outFile);
			}			
			success = p.createAndStartProcess(out, err);
			if (success) {
				final boolean hh = update();
				if (hh) {
					logger.debug(this, "updated databean");
				} else {
					logger.warn(this, "updating databean failed!");
				}
				success = hh;
			}
		} catch (Exception e) {
			logger.error(this, e.getLocalizedMessage(), e);
		}
		return success;
	}

	public synchronized void addShortCutFile(File file) {
		shortCutFiles.add(file);
	}
	
	public void setOutFile(File file){
		this.outFile  = file;
	}

	public abstract boolean update() throws StepExecutionException;

	private void validateProperties() throws StepExecutionException {
		if (!FileUtils.dirCheck(exeDir, false))
			throw new StepExecutionException("cannot access exe dir");
		if (!FileUtils.dirCheck(workingDir, true))
			throw new StepExecutionException("cannot access working dir");
	}

	private void printProperties() {
		logger.debug(this, " created, properties:");
		logger.debug(this, "\tstepWorkingDir=" + workingDir);
		logger.debug(this, "\texeDir=" + exeDir);
	}

}
