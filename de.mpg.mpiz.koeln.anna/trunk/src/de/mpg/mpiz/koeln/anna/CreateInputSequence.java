package de.mpg.mpiz.koeln.anna;

import java.io.File;
import java.io.IOException;

import de.kerner.commons.eee.JSFUtil;
import de.kerner.commons.file.FileUtils;
import de.kerner.commons.workflow.WorkFlowElement;
import de.kerner.commons.workflow.WorkFlowException;

public class CreateInputSequence implements WorkFlowElement {

	private final File sessionDir;
	
	public CreateInputSequence(File sessionDir) {
		this.sessionDir = sessionDir;
	}

	public void work() throws WorkFlowException {
		// assume, that check for file has already be performed
		try {
		final UploadBacking u = JSFUtil.getManagedObject(UploadBacking
				.getIdentString(), UploadBacking.class);
		final File inputSeq = new File(sessionDir, "anna/input/inputSeq.fasta");
			FileUtils.writeStreamToFile(u.getMyFile().getInputStream(), inputSeq);
		} catch (IOException e) {
			throw new InformByFacesMessageWorkFlowException(e.getLocalizedMessage(), e);
		}
	}

}
