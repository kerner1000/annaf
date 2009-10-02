package de.mpg.mpiz.koeln.anna.abstractstep;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

/**
 * <p> Helper class to create a WrapperStep for an external programm.</p>
 * @author Alexander Kerner
 * @param <T> type of {@link de.mpg.mpiz.koeln.anna.server.data.DataBean}
 */
public abstract class AbstractWrapperStep<T> extends AbstractAnnaStep<T> {

	protected volatile File exeDir;
	protected volatile File workingDir;
	private volatile File outFile = null;
	private volatile File errFile = null;
	protected List<File> shortCutFiles = new ArrayList<File>();

	/**
	 * <p>
	 * Finally start this Step.
	 * </p>
	 * 
	 * @throws StepExecutionException
	 */
	public boolean start() throws StepExecutionException {
		boolean success = false;
		try{
		printProperties();
		validateProperties();
		prepare();
		if(takeShortCut()){
			success = true;
		} else {
			success = doItFinally();
		}
		}catch (Exception e) {
			StepUtils.handleException(this, e);
		}
		return success;
	}

	public synchronized void addShortCutFile(File file) {
		shortCutFiles.add(file);
	}

	public void redirectOutStreamToFile(File file) {
		this.outFile = file;
	}
	
	public void redirectErrStreamToFile(File file) {
		this.errFile = file;
	}

	private boolean doItFinally() throws StepExecutionException {
		boolean success = false;
		try {
			final AbstractStepProcessBuilder p = new AbstractStepProcessBuilder(
					exeDir, workingDir, logger) {
				@Override
				protected List<String> getCommandList() {
					return getCmdList();
				}
			};
			OutputStream out = System.out;
			OutputStream err = System.err;
			if (outFile != null) {
				out = FileUtils.getOutputStreamForFile(outFile);
			}
			if (errFile != null) {
				err = FileUtils.getOutputStreamForFile(errFile);
			}
			success = p.createAndStartProcess(out, err);
			if (outFile != null) {
				out.close();
			}
			if (errFile != null) {
				err.close();
			}
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
			StepUtils.handleException(this, e);
		}
		return success;
	}

	private synchronized boolean takeShortCut() {
		logger.debug(this, "checking for shortcut available");
		if (shortCutFiles.isEmpty()) {
			logger.debug(this, "no shortcut files defined");
			return false;
		}
		for (File f : shortCutFiles) {
			final boolean fileCheck = FileUtils.fileCheck(f, false);
			logger.debug(this, "file " + f.getAbsolutePath() + " there="
					+ fileCheck);
			if (!(fileCheck)) {
				logger.debug(this, "cannot skip");
				return false;
			}
		}
		logger.debug(this, "skip available");
		return true;
	}

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

	public abstract boolean update() throws StepExecutionException;

}
