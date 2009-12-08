package de.mpg.mpiz.koeln.anna.step.repeatmasker.lsf;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.anna.step.common.lsf.LSF;
import de.mpg.mpiz.koeln.anna.step.repeatmasker.common.AbstractStepRepeatMasker;
import de.mpg.mpiz.koeln.anna.step.repeatmasker.common.RepeatMaskerConstants;

public class RepeatMaskerLSF extends AbstractStepRepeatMasker {

	@Override
	public List<String> getCmdList() {
		final CommandStringBuilder builder = new CommandStringBuilder(LSF.BSUB_EXE);
		builder.addAllFlagCommands(LSF.getBsubFlagCommandStrings());
		builder.addAllValueCommands(LSF.getBsubValueCommandStringsFailSave(workingDir));
		builder.addFlagCommand(new File(
				exeDir, RepeatMaskerConstants.EXE).getAbsolutePath());
//		builder.addAllFlagCommands("-s");
		builder.addFlagCommand("-gff");
		builder.addFlagCommand(inFile.getAbsolutePath());
		return builder.getCommandList();
	}	
}
