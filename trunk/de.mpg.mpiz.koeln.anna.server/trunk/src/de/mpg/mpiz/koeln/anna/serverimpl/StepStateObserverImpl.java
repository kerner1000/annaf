package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.step.Step;

/**
 * @ThreadSave
 * @cleaned 2009-07.29
 * @author Alexander Kerner
 *
 */
public class StepStateObserverImpl implements StepStateObserver {

	private final Map<Step, Step.State> stepStates = new HashMap<Step, Step.State>();
	private final Map<Step, Boolean> stepSuccesses = new HashMap<Step, Boolean>();
	private final static String PRE_LINE =  "++++++ current states ++++++++";
	private final static String POST_LINE = "++++++++++++++++++++++++++++++";
	private final LogDispatcher logger;
	
	public StepStateObserverImpl(LogDispatcher logger) {
		this.logger = logger;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	private synchronized void printStepStates(Step lastChangedStep) {
		logger.info(this, FileUtils.NEW_LINE);
		logger.info(this, PRE_LINE);
		
		for (Step s : stepStates.keySet()) {
			final String s1 = s.toString();
			final String s2 = "state=" + stepStates.get(s);
			final String s3 = "skipped=" + s.wasSkipped();
			String s4 = "success=" + stepSuccesses.get(s);
			
			final StringBuilder sb = new StringBuilder();
			// TODO better: String.format();
			final Formatter f = new Formatter();
			sb.append(f.format("\t%-28s\t%-22s\t%-10s\t%-10s", s1, s2, s3, s4).toString());
			
			if (lastChangedStep.equals(s)) {
				sb.append("\t(changed)");
			}
//			sb.append(FileUtils.NEW_LINE);
			logger.info(this, sb.toString());
		}
		logger.info(this, POST_LINE);
		logger.info(this, FileUtils.NEW_LINE);
	}

	private void changeStepState(Step step,
			Step.State newState) {
		if(step.getState().equals(Step.State.ERROR))
			newState = Step.State.ERROR;
		stepStates.put(step, newState);
		printStepStates(step);
	}

	public synchronized void stepFinished(Step step, boolean success) {
		final Step.State newState = Step.State.DONE;
//		final Step.State expectedCurrentState = State.RUNNING;
		stepSuccesses.put(step, success);
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

	public synchronized void stepRegistered(Step step) {
		final Step.State newState = Step.State.REGISTERED;
//		final Step.State expectedCurrentState = Step.State.LOOSE;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);

	}

	public synchronized void stepStarted(Step step) {
		final Step.State newState = Step.State.RUNNING;
//		final Step.State expectedCurrentState = Step.State.WAIT_FOR_REQ;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

	public synchronized void stepChecksNeedToRun(Step step) {
		final Step.State newState = Step.State.CHECK_NEED_TO_RUN;
//		final Step.State expectedCurrentState = Step.State.REGISTERED;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);

	}

	public synchronized void stepWaitForReq(Step step) {
		final Step.State newState = Step.State.WAIT_FOR_REQ;
//		final Step.State expectedCurrentState = Step.State.CHECK_NEED_TO_RUN;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

}
