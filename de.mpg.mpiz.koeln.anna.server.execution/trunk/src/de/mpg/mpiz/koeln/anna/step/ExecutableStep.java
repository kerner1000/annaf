package de.mpg.mpiz.koeln.anna.step;

import java.util.Properties;


public interface ExecutableStep {

	boolean requirementsSatisfied() throws StepExecutionException;

	boolean canBeSkipped() throws StepExecutionException;

	boolean run() throws StepExecutionException;
	
	Properties getStepProperties();
	
	boolean isCyclic();

}
