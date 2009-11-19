package de.mpg.mpiz.koeln.anna.step.conrad.train.lsf;

import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.anna.step.common.lsf.LSF;
import de.mpg.mpiz.koeln.anna.step.conrad.common.ConradConstants;
import de.mpg.mpiz.koeln.anna.step.conrad.train.common.AbstractTrain;

/**
 * @cleaned 2009-07-28
 * @author Alexander Kerner
 * 
 */
public class TrainLSF extends AbstractTrain {

	// Implement //

	@Override
	public List<String> getCmdList() {
		final CommandStringBuilder builder = new CommandStringBuilder(
				LSF.BSUB_EXE);
		builder.addAllFlagCommands(LSF.getBsubFlagCommandStrings());
		builder.addAllValueCommands(LSF.getBsubValueCommandStrings(workingDir));
		builder.addAllFlagCommands(ConradConstants.getConradCmdString());
		builder.addFlagCommand("train");
		builder.addFlagCommand("models/singleSpecies.xml");
		builder.addFlagCommand(workingDir.getAbsolutePath());
		builder.addFlagCommand(TRAINING_FILE.getAbsolutePath());
		return builder.getCommandList();

	}
}
