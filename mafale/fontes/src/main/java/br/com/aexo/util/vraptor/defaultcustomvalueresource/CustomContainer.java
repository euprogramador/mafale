package br.com.aexo.util.vraptor.defaultcustomvalueresource;

import org.springframework.web.context.ConfigurableWebApplicationContext;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.ioc.spring.SpringBasedContainer;

/**
 * container customizado para registrar cruds dinamicos
 * 
 * @author carlosr
 * 
 */
@Component
@ApplicationScoped
public class CustomContainer extends SpringBasedContainer implements Container {

	private final CrudService cruds;

	public CustomContainer(ConfigurableWebApplicationContext parentContext, CrudService cruds) {
		super(parentContext);
		this.cruds = cruds;
	}

	@Override
	public <T> T instanceFor(Class<T> type) {
		if (cruds.contains(type)) {
			if (!canProvide(type)) {
				register(type, type);
			}
		}

		return super.instanceFor(type);
	}

}
