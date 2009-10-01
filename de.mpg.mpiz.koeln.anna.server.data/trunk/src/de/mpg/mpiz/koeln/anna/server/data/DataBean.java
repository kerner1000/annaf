package de.mpg.mpiz.koeln.anna.server.data;

import java.io.Serializable;

public interface DataBean extends Serializable {
	
	public Serializable getCustom(String ident);
	
	public void addCustom(String ident, Serializable custom);

}
