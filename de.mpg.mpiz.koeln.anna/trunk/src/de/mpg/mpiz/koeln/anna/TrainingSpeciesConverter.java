package de.mpg.mpiz.koeln.anna;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

public class TrainingSpeciesConverter implements Converter {

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
			throws ConverterException {
		return arg1;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ConverterException {
		return arg2.toString();
	}

}
