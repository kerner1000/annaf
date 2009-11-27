package de.mpg.mpiz.koeln.anna;

import de.kerner.commons.eee.JSFUtil;
import de.kerner.commons.workflow.WorkFlowElement;
import de.kerner.commons.workflow.WorkFlowException;

public class CheckIfFileWasUploaded implements WorkFlowElement {

	public void work() throws WorkFlowException {
		final UploadBacking u = JSFUtil.getManagedObject(UploadBacking
				.getIdentString(), UploadBacking.class);
		if (u.getMyFile() == null) {
			throw new InformByFacesMessageWorkFlowException("No Sequence file provided");
		}
	}
}
