package de.mpg.mpiz.koeln.anna.step.conrad.common;

import java.io.File;

import org.osgi.framework.BundleContext;

import de.kerner.commons.StringUtils;
import de.mpg.mpiz.koeln.anna.step.AbstractStep;
import de.mpg.mpiz.koeln.anna.step.common.AbstractStepProcessBuilder;
import de.mpg.mpiz.koeln.anna.step.common.StepExecutionException;

/**
 * @lastVisit 2009-08-12
 * @ThreadSave custom
 * @author Alexander Kerner
 * @Exceptions try without
 * @Strings good
 *
 */
public abstract class AbstractConradStep extends AbstractStep {
	
	// assigned in init(), after that only read 
	protected File exeDir;
	
	// TODO dangerous. must be initialized by extending class
	// TODO not synchronized
	protected AbstractStepProcessBuilder process;
	
	// TODO dangerous. must be initialized by extending class
	protected File workingDir;
	
	protected synchronized void init(BundleContext context) throws StepExecutionException {
			super.init(context);
		exeDir = new File(super.getStepProperties()
				.getProperty(ConradConstants.CONRAD_DIR_KEY));
		logger.debug(this, StringUtils.getString("got exe dir=",exeDir));
	}
	
	protected abstract AbstractStepProcessBuilder getProcess();
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
