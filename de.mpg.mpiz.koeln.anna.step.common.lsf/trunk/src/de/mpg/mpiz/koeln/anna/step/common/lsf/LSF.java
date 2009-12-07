package de.mpg.mpiz.koeln.anna.step.common.lsf;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Alexander Kerner
 * @ThreadSave stateless
 * @lastVisit 2009-08-12
 * @Exceptions nothing to do
 *
 */
public class LSF {
	
	public final static String BSUB_EXE = "bsub";
	
	private LSF(){}
	
	public static Map<String, String> getBsubValueCommandStringsFailSave(File workingDir) {
		final File LSFout = new File(workingDir, "lsf-%J-%I.out");
		final File LSFerr = new File(workingDir, "lsf-%J-%I.err");
		final Map<String, String> map = new HashMap<String,String>();
//		map.put("-m", "wspcb015.mpiz-koeln.mpg.de");
		map.put("-q", "ubuntutest");
		map.put("-R", "rusage[mem=2000]");
		map.put("-eo", LSFerr.getAbsolutePath());
		map.put("-oo", LSFout.getAbsolutePath());
		return map;
	}
	
	public static Map<String, String> getBsubValueCommandStrings(File workingDir) {
		final File LSFout = new File(workingDir, "lsf-%J-%I.out");
		final File LSFerr = new File(workingDir, "lsf-%J-%I.err");
		final Map<String, String> map = new HashMap<String,String>();
		map.put("-m", "pcbcomputenodes");
		map.put("-R", "rusage[mem=3000]");
		map.put("-eo", LSFerr.getAbsolutePath());
		map.put("-oo", LSFout.getAbsolutePath());
		return map;
	}

	public static List<String> getBsubFlagCommandStrings() {
		final List<String> list = new ArrayList<String>();
		list.add("-K");
		list.add("-r");
		return list;
	}
}
