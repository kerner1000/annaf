package de.mpg.mpiz.koeln.anna;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class SelectTrainingSpeciesBacking {
	
	private List<SelectItem> list = new ArrayList<SelectItem>();
	
	// Constructor //
	
	public SelectTrainingSpeciesBacking(){
		list.add(getSelectItemFromTrainingSpeciesBean(new FusariumBean()));
		list.add(getSelectItemFromTrainingSpeciesBean(new ArabidobsisBean()));
	}
	
	// Private //
	
	private SelectItem getSelectItemFromTrainingSpeciesBean(TrainingSpeciesBean b){
		return new SelectItem(b, b.getName());
	}
	
	// Getter / Setter //
	
	public int getSize() {
		return list.size();
	}

	public List<SelectItem> getList() {
		return list;
	}

	public void setList(List<SelectItem> list) {
		this.list = list;
	}
	
}
