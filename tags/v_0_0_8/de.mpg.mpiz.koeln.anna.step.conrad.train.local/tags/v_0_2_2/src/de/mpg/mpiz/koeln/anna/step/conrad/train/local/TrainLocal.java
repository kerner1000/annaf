package de.mpg.mpiz.koeln.anna.step.conrad.train.local;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.anna.step.conrad.common.AbstractConradTrainStep;
import de.mpg.mpiz.koeln.anna.step.conrad.common.ConradConstants;

/**
 * @cleaned 0992-07-28
 * @author Alexander Kerner
 *
 */
public class TrainLocal extends AbstractConradTrainStep {

	private class Process extends AbstractStepProcessBuilder {

		protected Process(File executableDir, File workingDir, LogDispatcher logger) {
			super(executableDir, workingDir, logger);
		}

		@Override
		protected List<String> getCommandList() {
			final CommandStringBuilder builder = new CommandStringBuilder(
					new File(executableDir, ConradConstants.CONRAD_EXE)
							.getAbsolutePath());
			builder.addFlagCommand("train");
			builder.addFlagCommand("models/singleSpecies.xml");
			builder.addFlagCommand(workingDir.getAbsolutePath());
			builder.addFlagCommand(trainingFile.getAbsolutePath());
			return builder.getCommandList();
		}
	}

	@Override
	protected AbstractStepProcessBuilder getProcess() {
		final Process p = new Process(exeDir.getAbsoluteFile(), workingDir.getAbsoluteFile(), logger);
//		p.addResultFile(true, trainingFile);
		return p;
	}
}