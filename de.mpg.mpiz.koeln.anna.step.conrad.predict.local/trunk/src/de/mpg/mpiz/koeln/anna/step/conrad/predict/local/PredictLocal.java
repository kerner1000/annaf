package de.mpg.mpiz.koeln.anna.step.conrad.predict.local;

import java.io.File;
import java.util.List;

import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.anna.step.conrad.common.ConradConstants;
import de.mpg.mpiz.koeln.anna.step.conrad.predict.common.AbstractPredict;

/**
 * @author Alexander Kerner
 * 
 */
public class PredictLocal extends AbstractPredict {

	@Override
	public List<String> getCmdList() {

		return new CommandStringBuilder(ConradConstants.getConradCmdString())
				.addFlagCommand("predict").addFlagCommand(
						trainingFile.getAbsolutePath()).addFlagCommand(
						workingDir.getAbsolutePath()).addFlagCommand(
						resultFile.getParentFile().getAbsolutePath()
								+ File.separator + "result").getCommandList();

	}

}
