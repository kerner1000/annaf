package de.mpg.mpiz.koeln.anna.step.repeatmasker.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.kerner.commons.file.AbstractLineByLineReader;
import de.kerner.commons.file.FileUtils;
import de.kerner.commons.file.LazyStringWriter;
/**
 * <p>
 * Try to change line from
 * <blockquote>
 * contig00001	RepeatMasker	similarity	3920	4097	33.7	+Target "Motif:(CGG)n" 3 180
 * </blockquote>
 * to
 * <blockquote>
 * contig00001	RepeatMasker	similarity	3920	4097	33.7	+	Target "Motif:(CGG)n" 3 180
 * </blockquote>
 * </p>
 * @author Alexander Kerner
 *
 */
class ResultsPreprocessor {
	
	private final static String PATTERN_PREFIX = ".+";
	private final static String PATTERN_POSTFIX = ".+";
	private final static Pattern P_PLUS = Pattern.compile(PATTERN_PREFIX
			+ "[\\+]Target" + PATTERN_POSTFIX, Pattern.CASE_INSENSITIVE);
	private final static Pattern P_MINUS = Pattern.compile(PATTERN_PREFIX
			+ "[-]Target" + PATTERN_POSTFIX, Pattern.CASE_INSENSITIVE);
	
	private class Hans extends AbstractLineByLineReader {
		
		private final List<String> lines = new ArrayList<String>();
		
		@Override
		public void handleLine(String line) {
			final Matcher m_plus = P_PLUS.matcher(line);
			final Matcher m_minus = P_MINUS.matcher(line);
			if(m_plus.matches()){
				lines.add(line.replace("+Target", "+\tTarget\t"));
			} else if(m_minus.matches()) {
				lines.add(line.replace("-Target", "-\tTarget\t"));
			}else {
				lines.add(line);
			}
		}
		
		public void write(File out) throws IOException{
			final StringBuilder b = new StringBuilder(lines.size());
			for(String s : lines){
				b.append(s);
				b.append(FileUtils.NEW_LINE);
			}
			new LazyStringWriter(b.toString()).write(out);
		}
	}
	
	public void process(File in, File out) throws IOException{
		final Hans hans = new Hans();
		hans.read(in);
		hans.write(out);
	}
	
	public static void main(String[] args){
		File inFile = new File("/home/pcb/kerner/anna6/repeatMasker/repMask.out.gff.bak");
		File outFile = new File("/home/pcb/kerner/anna6/repeatMasker/repMask.out.gff3");
		try {
			new ResultsPreprocessor().process(inFile, outFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
