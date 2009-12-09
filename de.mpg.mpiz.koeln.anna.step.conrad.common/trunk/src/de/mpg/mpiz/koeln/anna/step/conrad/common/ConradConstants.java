package de.mpg.mpiz.koeln.anna.step.conrad.common;

import java.util.List;

import de.kerner.commons.CommandStringBuilder;

/**
 * 
 * @cleaned 2009-07-28
 * @author Alexander Kerner
 *
 */
public class ConradConstants {
	
	private ConradConstants(){}
	
	public final static List<String> getConradCmdString(){
		return new CommandStringBuilder("java").addFlagCommand("-Xmx10000m").addValueCommand("-jar", "conradCustom.jar").getCommandList();
	}
	
	public final static String TRAINING_FILE_KEY = "trainingFile";
	
	public final static String ADAPTED_KEY = "adapted";
	
	public final static String WORKING_DIR = "data/conrad";
	
	public final static String PROPERTIES_KEY_PREFIX = "anna.step.conrad.";
	
	public static final String TRAINING_FILE_REFIX = "conrad.trainingfile";

}
