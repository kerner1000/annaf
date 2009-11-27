package de.mpg.mpiz.koeln.anna;

import java.io.File;
import java.io.IOException;

import de.kerner.commons.file.FileUtils;
import de.kerner.commons.workflow.WorkFlowElement;
import de.kerner.commons.workflow.WorkFlowException;

public class TrainingFileCopier implements WorkFlowElement {

	private final File sessionDir;
	private final File trainingFile;
	
	public TrainingFileCopier(File sessionDir, File trainingFile) {
		this.sessionDir = sessionDir;
		this.trainingFile = trainingFile;
	}

	public void work() throws WorkFlowException {
		final File tf = new File(sessionDir, "anna/data/conrad/trainingFile.bin");
		try {
			FileUtils.copyFile(trainingFile, tf);
		} catch (Exception e) {
			throw new InformByFacesMessageWorkFlowException(e.getLocalizedMessage(), e);
		}
	}

}
