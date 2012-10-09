package br.com.aexo.sim.core.faces.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.aexo.sim.core.util.StringTools;

@FacesConverter(forClass=LocalDateTime.class)
public class LocalDateTimeConverter implements Converter {

	DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (!StringTools.isNullOrBlank(arg2)){
			return formatter.parseLocalDateTime(arg2);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		LocalDateTime data = (LocalDateTime) arg2;
		return data.toString(formatter);
	}

}
