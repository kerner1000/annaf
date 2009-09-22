package de.mpg.mpiz.koeln.anna.step.conrad.train.lsf;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.anna.step.common.lsf.LSF;
import de.mpg.mpiz.koeln.anna.step.conrad.common.AbstractConradTrainStep;
import de.mpg.mpiz.koeln.anna.step.conrad.common.ConradConstants;

/**
 * @cleaned 2009-07-28
 * @author Alexander Kerner
 *
 */
public class TrainLSF extends AbstractConradTrainStep {

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
			builder.addFlagCommand("train");
			builder.addFlagCommand("models/singleSpecies.xml");
			builder.addFlagCommand(workingDir.getAbsolutePath());
			builder.addFlagCommand(trainingFile.getAbsolutePath());
			return builder.getCommandList();
		}
	}

	@Override
	protected AbstractStepProcessBuilder getProcess() {
		final Process p = new Process(exeDir, workingDir, logger);
//		p.addResultFile(true, trainingFile);
		return p;
	}
}
