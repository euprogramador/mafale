package br.com.aexo.sim.core.faces.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.aexo.sim.core.util.StringTools;

@FacesConverter(forClass=LocalDate.class)
public class LocalDateConverter implements Converter {

	DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (!StringTools.isNullOrBlank(arg2)){
			return formatter.parseLocalDate(arg2);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		LocalDate data = (LocalDate) arg2;
		return data.toString(formatter);
	}

}
