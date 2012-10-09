package br.com.aexo.sim.core.faces.converter;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

@Named
public class Converters implements Converter {

	@SuppressWarnings("rawtypes")
	private List<?> entities = new ArrayList();

	public Converters create(List<?> entities){
		Converters e = new Converters();
		e.entities = entities;
		return e;
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return entities.get(new Integer(arg2));
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return new Integer(entities.indexOf(arg2)).toString();
	}

}