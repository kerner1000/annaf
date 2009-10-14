package de.mpg.mpiz.koeln.anna.step.exonerate.local;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.anna.step.exonerate.common.AbstractStepExonerate;
import de.mpg.mpiz.koeln.anna.step.exonerate.common.ExonerateConstants;

public class ExonerateLocal extends AbstractStepExonerate {

	@Override
	public List<String> getCmdList() {
		final CommandStringBuilder builder = new CommandStringBuilder(new File(
				exeDir, ExonerateConstants.EXE).getAbsolutePath());
		builder.addValueCommand("--showquerygff", "yes");
		builder.addValueCommand("--showalignment", "false");
		builder.addValueCommand("--bestn", "10");
		final String infile = new File(workingDir,
				ExonerateConstants.INSEQ_FILENAME).getAbsolutePath();
		builder.addFlagCommand(infile);
		final String ests = new File(workingDir,
				ExonerateConstants.EST_FILENAME).getAbsolutePath();
		builder.addFlagCommand(ests);
//		builder.addFlagCommand("> " + new File(workingDir, ExonerateConstants.RESULT_FILENAME).getAbsolutePath());
		return builder.getCommandList();
	}
}
