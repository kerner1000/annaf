package de.mpg.mpiz.koeln.anna.step.conrad.predict.lsf;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.anna.step.common.lsf.LSF;
import de.mpg.mpiz.koeln.anna.step.conrad.common.AbstractConradPredictStep;
import de.mpg.mpiz.koeln.anna.step.conrad.common.ConradConstants;

/**
 * @cleaned 2009-07-28
 * @author Alexander Kerner
 * 
 */
public class PredictLSF extends AbstractConradPredictStep {

	private class Process extends AbstractStepProcessBuilder {

		protected Process(File executableDir, File workingDir,
				LogDispatcher logger) {
			super(executableDir, workingDir, logger);
		}

		@Override
		protected List<String> getCommandList() {
			final CommandStringBuilder builder = new CommandStringBuilder(
					LSF.BSUB_EXE);
			builder.addAllFlagCommands(LSF.getBsubFlagCommandStrings());
			builder.addAllValueCommands(LSF
					.getBsubValueCommandStrings(workingDir));
			builder.addFlagCommand(ConradConstants.CONRAD_EXE);
			builder.addFlagCommand("predict");
			builder.addFlagCommand(trainingFile.getAbsolutePath());
			builder.addFlagCommand(workingDir.getAbsolutePath());

			// necessary, because "result" parameter will result in a file named
			// result.gtf. If we here hand over "result.gtf" we later receive
			// file named "result.gtf.gtf"
			builder.addFlagCommand(resultFile.getParentFile().getAbsolutePath() + File.separator + "result");
			return builder.getCommandList();
		}
	}

	@Override
	protected AbstractStepProcessBuilder getProcess() {
		return new Process(exeDir, workingDir, logger);
	}
}
