package de.mpg.mpiz.koeln.anna.server.data;

import java.io.Serializable;
import java.util.Map;

public interface DataBean extends Serializable {
	
	public Map<String, Serializable> getCustom();
	
	public void setCustom(Map<String, Serializable> custom);

}
