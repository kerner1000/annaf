package de.mpg.mpiz.koeln.anna;

import java.io.File;

import de.kerner.commons.eee.JSFUtil;
import de.kerner.commons.workflow.WorkFlowElement;
import de.kerner.commons.workflow.WorkFlowException;

public class SessionDirCreator implements WorkFlowElement {

	public void work() throws WorkFlowException {
		final ConfigBean config = JSFUtil.getManagedObject(ConfigBean
				.getIdentString(), ConfigBean.class);

		final String id = JSFUtil.getCurrentSession().getId();
		final String path = config.getWorkingDir();

		final File sessionDir = new File(path, id);

		if (sessionDir.exists()) {
			throw new InformByFacesMessageWorkFlowException("Currently only one job at a time per user is possible. Sorry!");
		}

		if (new File(path, id).mkdir()) {
			// all good
		} else {
			throw new InformByFacesMessageWorkFlowException("could not create dir \"" + new File(path, id) + "\"");
		}
	}
}
