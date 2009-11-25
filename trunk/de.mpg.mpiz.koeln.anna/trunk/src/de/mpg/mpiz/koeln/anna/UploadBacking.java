package de.mpg.mpiz.koeln.anna;

import java.io.File;

import org.apache.myfaces.custom.fileupload.UploadedFile;

import de.kerner.commons.file.HumanReadableFileSizePrinter;
import de.kerner.commons.logging.Log;

public class UploadBacking {
	
	private final static Log log = new Log(UploadBacking.class);
	
	 private UploadedFile myFile;

	public void setMyFile(UploadedFile myFile) {
		this.myFile = myFile;
	}

	public UploadedFile getMyFile() {
		return myFile;
	}
	
	public String getFileSize(){
		if(myFile == null)
			return null;
		final long size = myFile.getSize();
		return new HumanReadableFileSizePrinter(size, false).toString();
	}
}
