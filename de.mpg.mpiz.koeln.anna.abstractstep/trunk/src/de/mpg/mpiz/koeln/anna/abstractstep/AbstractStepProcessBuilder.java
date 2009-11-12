package de.mpg.mpiz.koeln.anna.abstractstep;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import de.kerner.commons.file.FileUtils;
import de.kerner.commons.logging.Log;

/**
 * @lastVisit 2009-08-12
 * @author Alexander Kerner
 * @ThreadSave state final, ConcurrentHashMap
 * @Exceptions nothing to do, abstract class
 *
 */
public abstract class AbstractStepProcessBuilder {

	private final static Log logger = new Log(AbstractStepProcessBuilder.class);
	
	protected final File executableDir;
	protected final File workingDir;
	
	// TODO: remove this. Let this completely be handled from "WrapperStep"
	private final Map<File, Boolean> outFiles = new ConcurrentHashMap<File, Boolean>();

	protected AbstractStepProcessBuilder(File executableDir, File workingDir) {
		this.executableDir = executableDir;
		this.workingDir = workingDir;
	}

	@Deprecated
	public void addResultFile(boolean takeShortCutIfAlreadyThere,
			String fileName) {
		addResultFile(takeShortCutIfAlreadyThere, new File(fileName));
	}
	
	@Deprecated
	public void addAllResultFiles(Map<File, Boolean> m) {
		if(m.isEmpty())
			return;
		outFiles.putAll(m);
	}
	
	@Deprecated
	public void addResultFile(boolean takeShortCutIfAlreadyThere, File file) {
		if (file == null)
			throw new NullPointerException(
					"file must not be null");
		outFiles
				.put(file, takeShortCutIfAlreadyThere);
	}

	public boolean createAndStartProcess(final OutputStream out,
			final OutputStream err) {
		if (takeShortCut()){
			logger.info("file(s) there, taking shortcut");
			return true;
		}
		logger.debug("file(s) not there, cannot take shortcut");
		final List<String> processCommandList = getCommandList();
		final ProcessBuilder processBuilder = new ProcessBuilder(
				processCommandList);
		logger.debug("creating process " + processBuilder.command());
		processBuilder.directory(executableDir);
		logger.debug("executable dir of process: " + processBuilder.directory());
		processBuilder.redirectErrorStream(true);
		try {
			Process p = processBuilder.start();
			logger.debug("started process " + p);
			FileUtils.inputStreamToOutputStream(p.getInputStream(), out);
			FileUtils.inputStreamToOutputStream(p.getErrorStream(), err);
			final int exit = p.waitFor();
//			logger.debug(this, "performing LSF buffer timeout...");
//			Thread.sleep(1000);
//			logger.debug(this, "continuing");
			logger.debug("process " + p + " exited with exit code " + exit);
			if (exit != 0)
				return false;
			return true;
		} catch (Exception e){
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return false;
		}
	}

	public boolean createAndStartProcess() {
		return createAndStartProcess(System.out, System.err);
	}
	
	@Deprecated
	private boolean takeShortCut() {
		logger.debug("checking for shortcut available");
		if(outFiles.isEmpty()){
			logger.debug("no outfiles defined");
			return false;
		}
		for (Entry<File, Boolean> e : outFiles.entrySet()) {
			final boolean fileCheck = FileUtils.fileCheck(e.getKey(), false);
			final boolean skipIt = e.getValue();
			logger.debug("file " + e.getKey().getAbsolutePath() + " there="+fileCheck);
			logger.debug("file " + e.getKey() + " skip="+skipIt);
			if(!(fileCheck && skipIt)){
				logger.debug("cannot skip");
				return false;
			}
		}
		logger.debug("skip available");
		return true;
	}
	
	protected abstract List<String> getCommandList();

}
