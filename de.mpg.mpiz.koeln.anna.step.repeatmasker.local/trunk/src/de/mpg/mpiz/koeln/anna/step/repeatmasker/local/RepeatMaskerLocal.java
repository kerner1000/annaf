package de.mpg.mpiz.koeln.anna.step.repeatmasker.local;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.anna.step.repeatmasker.common.AbstractStepRepeatMasker;
import de.mpg.mpiz.koeln.anna.step.repeatmasker.common.RepeatMaskerConstants;

public class RepeatMaskerLocal extends AbstractStepRepeatMasker {

	@Override
	public List<String> getCmdList() {
		final CommandStringBuilder builder = new CommandStringBuilder(new File(
				exeDir, RepeatMaskerConstants.EXE).getAbsolutePath());
		builder.addAllFlagCommands(RepeatMaskerConstants.OPTIONS_FLAG);
		builder.addAllValueCommands(RepeatMaskerConstants.OPTIONS_VALUE);
//		builder.addValueCommand("-pa", "2");
//		builder.addAllFlagCommands("-s");
//		builder.addFlagCommand("-gff");
//		builder.addFlagCommand("-qq");
		builder.addFlagCommand(inFile.getAbsolutePath());
		return builder.getCommandList();
	}	
}
