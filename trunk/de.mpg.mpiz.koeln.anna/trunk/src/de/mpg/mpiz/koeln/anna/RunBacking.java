package de.mpg.mpiz.koeln.anna;

import java.io.File;

import de.kerner.commons.eee.JSFUtil;
import de.kerner.commons.logging.Log;
import de.kerner.commons.workflow.WorkFlowException;

public class RunBacking {

	private final static Log log = new Log(RunBacking.class);

	private volatile File sessionDir = null;

	public synchronized String run() {
		assignSessionDir();

		final ExecutionWorkflow work = new ExecutionWorkflow();
		work.addElement(new CheckIfFileWasUploaded());
		work.addElement(new SessionDirCreator());
		work.addElement(new Extractor(sessionDir));
		work.addElement(new CreateInputSequence(sessionDir));
		work.addElement(new PropertiesAssigner(sessionDir));
		work
				.addElement(new TrainingFileCopier(
						sessionDir,
						new File(
								"/home/proj/kerner/diplom/anna/annaWorkingDir/trainingFiles/fusarium-graminearum/trainingFile.bin")));

		try {
			work.work();
		} catch (WorkFlowException e1) {
			final InformByFacesMessageWorkFlowException ex = (InformByFacesMessageWorkFlowException) e1;
			ex.doSomething();
			return "fail";
		}

		return "success";
	}

	private void assignSessionDir() {
		final ConfigBean config = JSFUtil.getManagedObject(ConfigBean
				.getIdentString(), ConfigBean.class);
		final String id = JSFUtil.getCurrentSession().getId();
		final String path = config.getWorkingDir();
		sessionDir = new File(path, id);
	}
}
