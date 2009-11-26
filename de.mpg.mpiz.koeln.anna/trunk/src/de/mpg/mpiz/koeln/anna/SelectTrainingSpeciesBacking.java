package de.mpg.mpiz.koeln.anna;

import java.util.ArrayList;
import java.util.List;

public class SelectTrainingSpeciesBacking {
	
	private List<TrainingSpeciesBean> list = new ArrayList<TrainingSpeciesBean>();
	
	public SelectTrainingSpeciesBacking(){
		list.add(new FusariumBean());
	}
	
	// Getter / Setter //
	
	public String getStringList() {
		return list.toString();
	}

	public List<TrainingSpeciesBean> getList() {
		return list;
	}

	public void setList(List<TrainingSpeciesBean> list) {
		this.list = list;
	}
	
}
