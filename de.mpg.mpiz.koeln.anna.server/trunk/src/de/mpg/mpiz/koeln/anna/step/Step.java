package de.mpg.mpiz.koeln.anna.step;

import java.util.Properties;

import de.mpg.mpiz.koeln.anna.server.dataproxy.DataProxy;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;
import de.mpg.mpiz.koeln.anna.step.common.StepProcessObserver;

public interface Step {
	
	public enum State {
		LOOSE, REGISTERED, CHECK_NEED_TO_RUN, WAIT_FOR_REQ, RUNNING, DONE,
		ERROR
	}

	/**
	 * No need for synchronization. DataProxy is fully threadSave.
	 * 
	 * @param data
	 * @return
	 * @throws StepExecutionException
	 */
	boolean requirementsSatisfied(DataProxy data) throws StepExecutionException;

	/**
	 * No need for synchronization. DataProxy is fully threadSave.
	 * 
	 * @param data
	 * @return
	 * @throws StepExecutionException
	 */
	boolean canBeSkipped(DataProxy data) throws StepExecutionException;

	/**
	 * No need for synchronization. DataProxy is fully threadSave.
	 * 
	 * @param data
	 * @return
	 * @throws StepExecutionException
	 */
	boolean run(DataProxy data) throws StepExecutionException;

	/**
	 * No need for synchronization. DataProxy is fully threadSave.
	 * 
	 * @param data
	 * @param listener
	 * @return
	 * @throws StepExecutionException
	 */
	boolean run(DataProxy data, StepProcessObserver listener)
			throws StepExecutionException;
	
	
	// maybe move these methods to other type ??
	
	
	State getState();
	
	boolean wasSkipped();
	
	void setSkipped(boolean skipped);
	
	void setState(State state);
	
	Properties getStepProperties();

}
