package de.mpg.mpiz.koeln.anna.step;

import java.util.Properties;

import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;

public interface ExecutableStep {

	boolean requirementsSatisfied() throws StepExecutionException;

	boolean canBeSkipped() throws StepExecutionException;

	boolean run() throws StepExecutionException;
	
	Properties getStepProperties();

}
