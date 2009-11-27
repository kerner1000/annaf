package de.mpg.mpiz.koeln.anna;

import java.io.File;

import de.kerner.commons.eee.JSFUtil;
import de.kerner.commons.workflow.WorkFlowElement;
import de.kerner.commons.workflow.WorkFlowException;
import de.kerner.commons.zip.ZipUtils;

public class Extractor implements WorkFlowElement {
	
	private final File dir;
	
	public Extractor(File dir){
		this.dir = dir;
	}

	public void work() throws WorkFlowException {
		final ConfigBean config = JSFUtil.getManagedObject(ConfigBean
				.getIdentString(), ConfigBean.class);

		final String path = config.getWorkingDir();
		final String zipName = config.getZipName();
		
		try {
			ZipUtils.extractToDir(new File(path, zipName), dir);
		} catch (Exception e) {
			throw new InformByFacesMessageWorkFlowException(e.getLocalizedMessage(), e);
		}
	}
}
