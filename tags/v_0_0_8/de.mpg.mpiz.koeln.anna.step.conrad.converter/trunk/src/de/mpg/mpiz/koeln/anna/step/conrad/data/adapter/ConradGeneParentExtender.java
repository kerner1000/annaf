package de.mpg.mpiz.koeln.anna.step.conrad.data.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.bioutils.Strand;
import de.bioutils.gff3.Type;
import de.bioutils.gff3.attribute.Attribute;
import de.bioutils.gff3.attribute.AttributeLine;
import de.bioutils.gff3.attribute.IDAttribute;
import de.bioutils.gff3.attribute.ParentAttribute;
import de.bioutils.gff3.converter.GFF3FileExtender;
import de.bioutils.gff3.element.GFF3Element;
import de.bioutils.gff3.element.GFF3ElementBuilder;
import de.bioutils.gff3.element.GFF3ElementGroup;
import de.bioutils.gff3.file.GFF3File;
import de.bioutils.gff3.file.GFF3FileImpl;

public class ConradGeneParentExtender implements GFF3FileExtender {

	public GFF3File extend(GFF3File gff3File) {
		final Map<IDAttribute, GFF3ElementGroup> mapToID = getElementsMappedToID(gff3File
				.getElements());
		final GFF3ElementGroup result = addParents(mapToID);
		return new GFF3FileImpl(result);
	}

	private Map<IDAttribute, GFF3ElementGroup> getElementsMappedToID(
			Collection<? extends GFF3Element> elements) {
		final Map<IDAttribute, GFF3ElementGroup> result = new HashMap<IDAttribute, GFF3ElementGroup>();
		for (GFF3Element e : elements) {
			final AttributeLine l = e.getAttributeLine();
			List<Attribute> atts = new ArrayList<Attribute>();
			String id = null;
			for (Attribute a : l.getAttributes()) {
				// gene_id "contig00001G_1"; transcript_id "contig00001T_1.1";
				// LOGGER.info("attribute is " + a);
				if (a.getKey().equalsIgnoreCase("gene_id")) {
					id = a.getValue();
					atts.add(new IDAttribute(id));
				}
				atts.add(a);
			}
			if (result.containsKey(new IDAttribute(id))) {
				GFF3ElementGroup g = result.get(new IDAttribute(id));
				g.add(new GFF3ElementBuilder(e).setAttributeLine(
						new AttributeLine(atts)).build());
				result.put(new IDAttribute(id), g);
			} else {
				GFF3ElementGroup g = new GFF3ElementGroup();
				g.add(new GFF3ElementBuilder(e).setAttributeLine(
						new AttributeLine(atts)).build());
				result.put(new IDAttribute(id), g);
			}
		}
		return result;
	}

	private GFF3ElementGroup addParents(
			Map<IDAttribute, GFF3ElementGroup> mapToID) {
		// LOGGER.info("map " + mapToID);
		final GFF3ElementGroup result = new GFF3ElementGroup();
		for (Entry<IDAttribute, GFF3ElementGroup> e : mapToID.entrySet()) {
			final IDAttribute id = new IDAttribute(e.getKey().getValue() + "_gene");
			// LOGGER.info(e.getValue().toString());
			result.addAll(addParentTag(id, e.getValue()));
			final Strand s = getStrandForGene(e.getValue());
			result.add(new GFF3ElementBuilder(e.getValue().getElements()
					.iterator().next()).setType(Type.gene).setStart(e.getValue().getStart())
					.setStop(e.getValue().getStop()).setStrand(s).setAttributeLine(
							new AttributeLine(id)).build());
		}
		return result;
	}

	private Strand getStrandForGene(GFF3ElementGroup group) {
		Strand s = null;
		Strand last = null;
		for(GFF3Element e : group){
			last = s;
			s = e.getStrand();
			if(s.equals(last) || last == null){
				// all good
			} else {
//				logger.debug("gene orientation is not ambigious");
			}
		}
		return s;
	}

	private GFF3ElementGroup addParentTag(IDAttribute parentid, GFF3ElementGroup group) {
		final GFF3ElementGroup result = new GFF3ElementGroup();
		for(GFF3Element e : group){
			final Collection<Attribute> nl = new ArrayList<Attribute>();
//			System.out.println(parentid.getValue());
			nl.add(new ParentAttribute(parentid.getValue()));
			result.add(new GFF3ElementBuilder(e).setAttributeLine(new AttributeLine(nl)).build());
		}
//		System.out.println(result);
		return result;
	}

	
}
