package de.mpg.mpiz.koeln.anna.step.repeatmasker.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RepeatMaskerConstants {
	
	private RepeatMaskerConstants(){}
	
	public final static String WORKING_DIR_KEY = "anna.step.repeatMasker.workingDir";
	public final static String EXE_DIR_KEY = "anna.step.repeatMasker.exeDir";
	public final static String TMP_FILENAME = "repMask";
	public final static String OUTFILE_POSTFIX = ".out.gff";
	public final static String EXE = "RepeatMasker";
	public final static String OUTSTREAM_FILE_KEY = "anna.step.repeatMasker.outstream.file";
	public final static List<String> OPTIONS_FLAG = new ArrayList<String>(){
		private static final long serialVersionUID = 1984766436592060144L;
		{
			add("-gff");
		}
	};
	// TODO: this is c. higginsianum (and path) specific.
	public final static Map<String, String> OPTIONS_VALUE = new LinkedHashMap<String, String>(){
		private static final long serialVersionUID = 1984766436592060144L;
		{
			put("-lib", "/opt/share/local/users/kerner/repeatmasker/Libraries/c.higginsianum.lib");
		}
	};

}
