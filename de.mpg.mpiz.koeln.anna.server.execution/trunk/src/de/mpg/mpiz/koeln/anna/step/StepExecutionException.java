package de.mpg.mpiz.koeln.anna.step;


public class StepExecutionException extends Exception {

	private static final long serialVersionUID = 5683856650378220175L;
	private final ExecutableStep step;

	public StepExecutionException(ExecutableStep step) {
		this.step = step;
	}

	public StepExecutionException(ExecutableStep step, String arg0) {
		super(arg0);
		this.step = step;
	}

	public StepExecutionException(ExecutableStep step, Throwable arg0) {
		super(arg0);
		this.step = step;
	}

	public StepExecutionException(ExecutableStep step, String arg0, Throwable arg1) {
		super(arg0, arg1);
		this.step = step;
	}
	
	public ExecutableStep getStep(){
		return step;
	}
}
