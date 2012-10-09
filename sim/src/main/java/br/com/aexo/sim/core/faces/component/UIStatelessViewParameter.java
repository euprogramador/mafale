package br.com.aexo.sim.core.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIViewParameter;

@FacesComponent("com.my.UIStatelessViewParameter")
public class UIStatelessViewParameter extends UIViewParameter {
 
    
    @Override
    public Object getLocalValue() {
    	return getSubmittedValue();
    }
}