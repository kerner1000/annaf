package de.mpg.mpiz.koeln.anna.step.conrad.predict.lsf;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.anna.step.common.lsf.LSF;
import de.mpg.mpiz.koeln.anna.step.conrad.common.ConradConstants;
import de.mpg.mpiz.koeln.anna.step.conrad.predict.common.AbstractPredict;

/**
 * @author Alexander Kerner
 * 
 */
public class PredictLSF extends AbstractPredict {

	@Override
	public synchronized List<String> getCmdList() {

		final CommandStringBuilder builder = new CommandStringBuilder(
				LSF.BSUB_EXE);
		builder.addAllFlagCommands(LSF.getBsubFlagCommandStrings());
		builder.addAllValueCommands(LSF.getBsubValueCommandStrings(workingDir));
		builder.addAllFlagCommands(ConradConstants.getConradCmdString());
		builder.addFlagCommand("predict");
		builder.addFlagCommand(trainingFile.getAbsolutePath());
		builder.addFlagCommand(workingDir.getAbsolutePath());

		// necessary, because "result" parameter will result in a file named
		// result.gtf. If we here hand over "result.gtf" we later receive
		// file named "result.gtf.gtf"
		builder.addFlagCommand(resultFile.getParentFile().getAbsolutePath()
				+ File.separator + "result");
		return builder.getCommandList();
	}

}
