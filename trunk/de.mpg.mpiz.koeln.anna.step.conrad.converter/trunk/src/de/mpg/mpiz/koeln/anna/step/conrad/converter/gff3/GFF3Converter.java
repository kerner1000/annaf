package de.mpg.mpiz.koeln.anna.step.conrad.converter.gff3;

import de.bioutils.gff.file.NewGFFFile;
import de.bioutils.gff3.file.GFF3File;

public interface GFF3Converter {

	NewGFFFile convert(GFF3File file);
	
}
