package de.mpg.mpiz.koeln.anna.abstractstep;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepUtils;

/**
 * <p>
 * Helper class to create a WrapperStep for an external programm.
 * </p>
 * 
 * @author Alexander Kerner
 * @threadSave custom
 * @lastVisit 2009-10-02
 * @param <T>
 *            type of {@link de.mpg.mpiz.koeln.anna.server.data.DataBean}
 */
public abstract class AbstractWrapperStep<T> extends AbstractAnnaStep<T> {
	
	private class ThreaddedProcess implements Callable<Boolean> {
		
		private final AbstractStepProcessBuilder ps;
		private final OutputStream out, err;
		
		ThreaddedProcess(File executableDir, File workingDir, OutputStream out, OutputStream err,
				LogDispatcher logger){
			this.out = out;
			this.err = err;
			ps = new AbstractStepProcessBuilder(
					executableDir, workingDir, logger) {
				@Override
				protected List<String> getCommandList() {
					return getCmdList();
				}
			};
		}

		public Boolean call() throws Exception {
			return ps.createAndStartProcess(out, err);
		}
		
	}

	private final ExecutorService exe = Executors.newSingleThreadExecutor();
	private final static long TIMEOUT = 1000;
	protected volatile File exeDir;
	protected volatile File workingDir;
	private volatile File outFile = null;
	private volatile File errFile = null;
	protected List<File> shortCutFiles = new ArrayList<File>();
	protected List<File> resultFilesToWaitFor = new ArrayList<File>();

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
			if (takeShortCut()) {
				success = true;
			} else {
				success = doItFinally();
			}
			if (success) {
				waitForFiles();
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

	private void waitForFiles() throws InterruptedException {
		if(resultFilesToWaitFor.isEmpty()){
			logger.debug(this, "no files to wait for");
			return;
		}
		for(File f : resultFilesToWaitFor){
			synchronized (f) {
				while(!f.exists()){
					logger.debug(this, "waiting for file \"" + f + " \"");
					Thread.sleep(TIMEOUT);
				}
			}
		}
	}

	// fields volatile
	private boolean doItFinally() throws StepExecutionException {
		boolean success = false;
		try {
			OutputStream out = System.out;
			OutputStream err = System.err;
			if (outFile != null) {
				out = FileUtils.getBufferedOutputStreamForFile(outFile);
			}
			if (errFile != null) {
				err = FileUtils.getBufferedOutputStreamForFile(errFile);
			}
			success = exe.submit(new ThreaddedProcess(exeDir, workingDir, out, err, logger)).get();
			if (outFile != null) {
				out.close();
			}
			if (errFile != null) {
				err.close();
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
	
	public synchronized void addShortCutFile(File file) {
		shortCutFiles.add(file);
	}
	
	public synchronized void addResultFileToWaitFor(File file) {
		resultFilesToWaitFor.add(file);
	}

	public void redirectOutStreamToFile(File file) {
		this.outFile = file;
	}

	public void redirectErrStreamToFile(File file) {
		this.errFile = file;
	}
	
	// fields volatile
	private void printProperties() {
		logger.debug(this, " created, properties:");
		logger.debug(this, "\tstepWorkingDir=" + workingDir);
		logger.debug(this, "\texeDir=" + exeDir);
	}
	
	// fields volatile
	private void validateProperties() throws StepExecutionException {
		if (!FileUtils.dirCheck(exeDir, false))
			throw new StepExecutionException(this, "cannot access exe dir");
		if (!FileUtils.dirCheck(workingDir, true))
			throw new StepExecutionException(this, "cannot access working dir");
	}
}
