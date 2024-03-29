package de.mpg.mpiz.koeln.anna.abstractstep;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.kerner.commons.file.FileUtils;
import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.server.data.DataProxy;
import de.mpg.mpiz.koeln.anna.step.StepExecutionException;

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
 * 
 */
public abstract class AbstractWrapperStep<T> extends AbstractAnnaStep<T> {

	private class ThreaddedProcess implements Callable<Boolean> {

		private final AbstractStepProcessBuilder ps;
		private final OutputStream out, err;

		ThreaddedProcess(File executableDir, File workingDir, OutputStream out,
				OutputStream err, Log logger) {
			this.out = out;
			this.err = err;
			ps = new AbstractStepProcessBuilder(executableDir, workingDir) {
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
	private List<File> shortCutFiles = new ArrayList<File>();
	private List<File> resultFilesToWaitFor = new ArrayList<File>();
	
	public AbstractWrapperStep(File exeDir, File workingDir) {
		this.exeDir = exeDir;
		this.workingDir = workingDir;
	}
	
	public AbstractWrapperStep() {
		
	}

	/**
	 * <p>
	 * Finally start this Step.
	 * </p>
	 * 
	 * @throws StepExecutionException
	 */
	public boolean start() throws Throwable {
		boolean success = false;

		createIfAbsend();
		prepare(getDataProxy());
		printProperties();
		validateProperties();
		if (takeShortCut()) {
			success = true;
		} else {
			success = doItFinally();
		}
		if (success) {
			waitForFiles();
			update(getDataProxy());
		}
		return success;
	}

	protected void createIfAbsend() throws StepExecutionException {
		if (!FileUtils.dirCheck(workingDir, true))
			throw new StepExecutionException(this,
					"cannot access working dir \"" + workingDir + "\"");
		if (!FileUtils.dirCheck(exeDir, true))
			throw new StepExecutionException(this,
					"cannot access executable dir \"" + exeDir + "\"");

	}

	private void waitForFiles() throws InterruptedException {
		if (resultFilesToWaitFor.isEmpty()) {
			logger.debug("no files to wait for");
			return;
		}
		for (File f : resultFilesToWaitFor) {
			synchronized (f) {
				while (!f.exists()) {
					logger.debug("waiting for file \"" + f + " \"");
					Thread.sleep(TIMEOUT);
				}
			}
		}
	}

	// fields volatile
	private boolean doItFinally() throws Throwable {
		boolean success = false;
		OutputStream out = System.out;
		OutputStream err = System.err;
		if (outFile != null) {
			out = FileUtils.getBufferedOutputStreamForFile(outFile);
		}
		if (errFile != null) {
			err = FileUtils.getBufferedOutputStreamForFile(errFile);
		}
		success = exe.submit(
				new ThreaddedProcess(exeDir, workingDir, out, err, logger))
				.get();
		exe.shutdown();
		if (outFile != null) {
			out.close();
		}
		if (errFile != null) {
			err.close();
		}
		return success;
	}

	private synchronized boolean takeShortCut() {
		logger.debug("checking for shortcut available");
		if (shortCutFiles.isEmpty()) {
			logger.debug("no shortcut files defined");
			return false;
		}
		for (File f : shortCutFiles) {
			final boolean fileCheck = FileUtils.fileCheck(f, false);
			logger.debug("file " + f.getAbsolutePath() + " there=" + fileCheck);
			if (!(fileCheck)) {
				logger.debug("cannot skip");
				return false;
			}
		}
		logger.debug("skip available");
		return true;
	}

	/**
	 * <p>
	 * Preparation for running wrapped process. (e.g. creating required files in
	 * working directory)
	 * </p>
	 * 
	 * @throws Throwable 
	 */
	public abstract void prepare(DataProxy<T> data) throws Throwable;

	/**
	 * @return List of command line arguments, that will passed to wrapped step.
	 */
	public abstract List<String> getCmdList();

	public abstract void update(DataProxy<T> data)
	throws Throwable;

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
		logger.debug("created, properties:" + FileUtils.NEW_LINE
				+ "\tstepWorkingDir=" + workingDir + FileUtils.NEW_LINE
				+ "\texeDir=" + exeDir);
	}

	// fields volatile
	private void validateProperties() throws Throwable {
		if (!FileUtils.dirCheck(exeDir, false))
			throw new StepExecutionException(this, "cannot access exe dir");
		if (!FileUtils.dirCheck(workingDir, true))
			throw new StepExecutionException(this, "cannot access working dir");
	}
	
	public void setExeDir(File exeDir){
		this.exeDir = exeDir;
	}
	
	public void setWorkingDir(File workingDir){
		this.workingDir = workingDir;
	}
}
