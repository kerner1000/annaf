package de.mpg.mpiz.koeln.anna.serverimpl;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import de.kerner.commons.file.FileUtils;
import de.kerner.osgi.commons.logger.dispatcher.LogDispatcher;
import de.mpg.mpiz.koeln.anna.server.data.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.step.Step;

/**
 * @ThreadSave
 * @cleaned 2009-07.29
 * @author Alexander Kerner
 *
 */
public class StepStateObserverImpl implements StepStateObserver<GFF3DataBean> {

	private final Map<Step<GFF3DataBean>, Step.State> stepStates = new HashMap<Step<GFF3DataBean>, Step.State>();
	private final Map<Step<GFF3DataBean>, Boolean> stepSuccesses = new HashMap<Step<GFF3DataBean>, Boolean>();
	private final static String PRE_LINE =  "++++++ current states ++++++++";
	private final static String POST_LINE = "++++++++++++++++++++++++++++++";
	private final LogDispatcher logger;
	
	public StepStateObserverImpl(LogDispatcher logger) {
		this.logger = logger;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	private synchronized void printStepStates(Step<GFF3DataBean> lastChangedStep) {
		logger.info(this, FileUtils.NEW_LINE);
		logger.info(this, PRE_LINE);
		
		for (Step<GFF3DataBean> s : stepStates.keySet()) {
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

	private void changeStepState(Step<GFF3DataBean> step,
			Step.State newState) {
		if(step.getState().equals(Step.State.ERROR))
			newState = Step.State.ERROR;
		stepStates.put(step, newState);
		printStepStates(step);
	}

	public synchronized void stepFinished(Step<GFF3DataBean> step, boolean success) {
		final Step.State newState = Step.State.DONE;
//		final Step.State expectedCurrentState = State.RUNNING;
		stepSuccesses.put(step, success);
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

	public synchronized void stepRegistered(Step<GFF3DataBean> step) {
		final Step.State newState = Step.State.REGISTERED;
//		final Step.State expectedCurrentState = Step.State.LOOSE;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);

	}

	public synchronized void stepStarted(Step<GFF3DataBean> step) {
		final Step.State newState = Step.State.RUNNING;
//		final Step.State expectedCurrentState = Step.State.WAIT_FOR_REQ;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

	public synchronized void stepChecksNeedToRun(Step<GFF3DataBean> step) {
		final Step.State newState = Step.State.CHECK_NEED_TO_RUN;
//		final Step.State expectedCurrentState = Step.State.REGISTERED;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);

	}

	public synchronized void stepWaitForReq(Step<GFF3DataBean> step) {
		final Step.State newState = Step.State.WAIT_FOR_REQ;
//		final Step.State expectedCurrentState = Step.State.CHECK_NEED_TO_RUN;
//		checkConsistity(step, expectedCurrentState, newState);
		changeStepState(step, newState);
	}

}
