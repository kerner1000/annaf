package de.mpg.mpiz.koeln.anna.step.exonerate.lsf;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.anna.step.common.lsf.LSF;
import de.mpg.mpiz.koeln.anna.step.exonerate.common.AbstractStepExonerate;
import de.mpg.mpiz.koeln.anna.step.exonerate.common.ExonerateConstants;

public class ExonerateLSF extends AbstractStepExonerate {

	@Override
	public List<String> getCmdList() {
		final CommandStringBuilder builder = new CommandStringBuilder(LSF.BSUB_EXE);
		builder.addAllFlagCommands(LSF.getBsubFlagCommandStrings());
		builder.addAllValueCommands(LSF.getBsubValueCommandStringsFailSave(workingDir));
		builder.addFlagCommand(new File(
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
		builder.addFlagCommand(">" + new File(workingDir, ExonerateConstants.RESULT_FILENAME).getAbsolutePath());
		return builder.getCommandList();
	}

}
