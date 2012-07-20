package br.com.aexo.util.vraptor.converters;
import org.joda.time.LocalDate;

import br.com.caelum.vraptor.ioc.Component;

import com.thoughtworks.xstream.converters.SingleValueConverter;
  
@Component      
public class ConvertDataJSON implements SingleValueConverter  {     
  
    @Override  
    public String toString(Object value) {  
    	LocalDate d = new LocalDate(value);  
        String data = d.toString("dd/MM/yyyy");  
        return data;  
      
    }    
       
    @SuppressWarnings("rawtypes")  
    public boolean canConvert(Class clazz) {    
        return LocalDate.class.isAssignableFrom(clazz);  
    }  
  
    @Override  
    public Object fromString(String arg0) {  
        // TODO Auto-generated method stub
    	System.out.println(arg0);
        return null;  
    }    
    
}    