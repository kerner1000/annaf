package de.mpg.mpiz.koeln.anna;


public class ConfigBean {
	
	/**
	 * <p> For example "#{userBean}" </p>
	 */
	public static String getIdentString(){
		return "#{configBean}";
	}
	
	private String workingDir;
	
	private String zipName;

	private String trainingFileName;;

	public String getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}

	public String getZipName() {
		return zipName;
	}

	public void setZipName(String zipName) {
		this.zipName = zipName;
	}

	public String getTrainingFileName() {
		return trainingFileName;
	}

	public void setTrainingFileName(String trainingFileName) {
		this.trainingFileName = trainingFileName;
	}
	
}
