package de.mpg.mpiz.koeln.anna.step.conrad.data.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff.element.NewGFFElement;
import de.bioutils.gff.element.NewGFFElementBuilder;
import de.bioutils.gff.file.NewGFFFile;
import de.bioutils.gff.file.NewGFFFileImpl;
import de.bioutils.gff3.attribute.AttributeLine;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;

public class GFF3ConverterImpl implements GFF3Converter {

	public NewGFFFile convert(GFF3File file) {
		final ArrayList<NewGFFElement> result = new ArrayList<NewGFFElement>();
		for (GFF3Element e : file.getElements()) {
			final NewGFFElementBuilder b = new NewGFFElementBuilder(e);
			String s = e.getAttributeLine().toString();
			s = s.replaceAll("=", " ");
			final List<String> l = Arrays.asList(s.split(AttributeLine.ATTRIBUTE_SEPARATOR));
			b.setAttributes(new ArrayList<String>(l));
			result.add(b.build());
		}
		return new NewGFFFileImpl(result);
	}

	public static void main(String[] args) {
		final File f = new File("/home/pcb/kerner/Desktop/ref.gtf");
		final File f3 = new File("/home/pcb/kerner/Desktop/ref.new");
		try {
			final GFF3File f2 = GFF3FileImpl.convertFromGFF(f);
			final NewGFFFile f4 = new GFF3ConverterImpl().convert(f2);
			f4.write(f3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GFFFormatErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
