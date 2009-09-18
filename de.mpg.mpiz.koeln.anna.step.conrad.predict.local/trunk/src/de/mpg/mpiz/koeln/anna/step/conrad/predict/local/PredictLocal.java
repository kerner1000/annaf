package de.mpg.mpiz.koeln.anna.step.conrad.predict.local;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.anna.step.conrad.common.AbstractConradPredictStep;
import de.mpg.mpiz.koeln.anna.step.conrad.common.ConradConstants;

/**
 * @cleaned 2009-07-28
 * @author Alexander Kerner
 *
 */
public class PredictLocal extends AbstractConradPredictStep {
	
	private class Process extends AbstractStepProcessBuilder {

		protected Process(File executableDir, File workingDir,
				LogDispatcher logger) {
			super(executableDir, workingDir, logger);
		}

		@Override
		protected List<String> getCommandList() {
			final CommandStringBuilder builder = new CommandStringBuilder(new File(
					executableDir, ConradConstants.CONRAD_EXE).getAbsolutePath());
			builder.addFlagCommand("predict");
			builder.addFlagCommand(trainingFile.getAbsolutePath());
			builder.addFlagCommand(workingDir.getAbsolutePath());
			builder.addFlagCommand(resultFile.getParentFile().getAbsolutePath() + File.separator + "result");
			return builder.getCommandList();
		}

	}

	@Override
	protected AbstractStepProcessBuilder getProcess() {
		return new Process(exeDir, workingDir, logger);
	}
}
