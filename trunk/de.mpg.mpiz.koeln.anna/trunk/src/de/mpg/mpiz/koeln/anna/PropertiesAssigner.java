package de.mpg.mpiz.koeln.anna;

import java.io.File;
import java.util.Properties;

import de.kerner.commons.file.FileUtils;
import de.kerner.commons.workflow.WorkFlowElement;
import de.kerner.commons.workflow.WorkFlowException;

public class PropertiesAssigner implements WorkFlowElement {

	private final File sessionDir;

	public PropertiesAssigner(File sessionDir) {
		this.sessionDir = sessionDir;
	}

	public void work() throws WorkFlowException {
		final Properties p = new Properties();
		p.setProperty("anna.step.inputsequencereader.infile",
				"input/inputSeq.fasta");

		// not needed, since training will be skipped anyhow.
		p.setProperty("anna.step.verified.fasta", "input/dummy.fasta");
		p.setProperty("anna.step.verified.gtf", "input/dummy.gff3");

		// TODO adaptations necessary if we do not run training?
		p.setProperty("anna.step.conrad.adapter.offset", "200");
		p.setProperty("anna.step.conrad.adapter.maxElementLength", "5000");
		p.setProperty("anna.step.conrad.adapter.maxElementNumber", "5000");

		p.setProperty("anna.step.repeatMasker.workingDir", "repeatMasker/");
		p.setProperty("anna.step.repeatMasker.exeDir",
				"/opt/share/common/science/RepeatMasker/");
		p.setProperty("anna.step.repeatMasker.outstream.file", "out");
		p.setProperty("anna.step.getResults.outDir", "results/");
		p.setProperty("anna.step.getResults.fileName", "results.gff3");

		try {
			p.store(FileUtils.getOutputStreamForFile(new File(sessionDir,
					"anna/configuration/step.properties")), null);
		} catch (Exception e) {
			throw new InformByFacesMessageWorkFlowException(e
					.getLocalizedMessage(), e);
		}
	}

}
