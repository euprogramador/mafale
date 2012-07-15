package br.com.aexo.mafale;

import br.com.aexo.util.hibernate.interceptors.HibernateInterceptors;
import br.com.aexo.util.vraptor.SessionFactoryCreator;
import br.com.caelum.iogi.Instantiator;
import br.com.caelum.iogi.spi.DependencyProvider;
import br.com.caelum.iogi.spi.ParameterNamesProvider;
import br.com.caelum.vraptor.ComponentRegistry;
import br.com.caelum.vraptor.http.ParametersProvider;
import br.com.caelum.vraptor.http.iogi.IogiParametersProvider;
import br.com.caelum.vraptor.http.iogi.VRaptorDependencyProvider;
import br.com.caelum.vraptor.http.iogi.VRaptorInstantiator;
import br.com.caelum.vraptor.http.iogi.VRaptorParameterNamesProvider;
import br.com.caelum.vraptor.ioc.spring.SpringProvider;
import br.com.caelum.vraptor.serialization.xstream.XStreamBuilder;

/**
 * Configura o sistema
 * 
 * @author carlosr
 * 
 */
public class MafaleProvider extends SpringProvider {

	@Override
	protected void registerCustomComponents(ComponentRegistry registry) {
		registry.register(SessionFactoryCreator.class, SessionFactoryCreator.class);
		registry.register(ParametersProvider.class, IogiParametersProvider.class);
		registry.register(DependencyProvider.class, VRaptorDependencyProvider.class);
		registry.register(ParameterNamesProvider.class, VRaptorParameterNamesProvider.class);
		registry.register(Instantiator.class, VRaptorInstantiator.class);
		registry.register(HibernateInterceptors.class, MafaleHibernateInterceptors.class);
	}

}