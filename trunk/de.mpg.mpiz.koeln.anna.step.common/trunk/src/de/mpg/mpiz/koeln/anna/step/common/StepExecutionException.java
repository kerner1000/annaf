package de.mpg.mpiz.koeln.anna.step.common;

public class StepExecutionException extends Exception {

	private static final long serialVersionUID = 5683856650378220175L;

	public StepExecutionException() {
	}

	public StepExecutionException(String arg0) {
		super(arg0);
	}

	public StepExecutionException(Throwable arg0) {
		super(arg0);
	}

	public StepExecutionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
