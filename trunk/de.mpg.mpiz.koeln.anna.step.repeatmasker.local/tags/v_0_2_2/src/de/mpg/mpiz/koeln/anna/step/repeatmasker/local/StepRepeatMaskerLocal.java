package de.mpg.mpiz.koeln.anna.step.repeatmasker.local;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.anna.step.repeatmasker.common.AbstractStepRepeatMasker;
import de.mpg.mpiz.koeln.anna.step.repeatmasker.common.RepeatMaskerConstants;

public class StepRepeatMaskerLocal extends AbstractStepRepeatMasker {
	
	private class Process extends AbstractStepProcessBuilder {

		private final File inFile;

		protected Process(File executableDir, File stepWorkingDir,
				LogDispatcher logger, File inFile) {
			super(executableDir, stepWorkingDir, logger);
			this.inFile = inFile;
		}

		public String toString() {
			return this.getClass().getSimpleName();
		}
		
		@Override
		protected List<String> getCommandList() {
			// ./RepeatMasker -pa 2 -s -gff
			// /home/proj/kerner/diplom/conrad/trainingAndCrossValidationWithProvidedData/test3/ref.fasta
			final CommandStringBuilder builder = new CommandStringBuilder(new File(
					executableDir, RepeatMaskerConstants.EXE).getAbsolutePath());
//			builder.addValueCommand("-pa", "2");
//			builder.addAllFlagCommands("-s");
			builder.addFlagCommand("-gff");
			builder.addFlagCommand("-qq");
			builder.addFlagCommand(inFile.getAbsolutePath());
			return builder.getCommandList();
		}
	}

	@Override
	protected AbstractStepProcessBuilder getProcess(File inFile) {
		return new Process(exeDir, workingDir
				, logger, inFile);
	}	
}
