package br.com.aexo.util.hibernate.interceptors;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.Id;

import net.vidageek.mirror.dsl.Matcher;
import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;

import br.com.caelum.iogi.Instantiator;
import br.com.caelum.iogi.parameters.Parameters;
import br.com.caelum.iogi.reflection.Target;
import br.com.caelum.vraptor.ioc.Component;

/**
 * Interceptor do hibernate respnsavel buscar os objetos que serão entidades
 * pelo container de DI, fazendo com que as entidades recuperadas pelo banco
 * tenham acesso aos serviços do container
 * 
 * @author carlosr
 * 
 */
@Component
public class DependencyInjectionInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 5848222336834528934L;

	private final Instantiator<Object> instantiator;

	/**
	 * 
	 * @param instantiator
	 *            busca as dependencias no container
	 */
	public DependencyInjectionInterceptor(Instantiator<Object> instantiator) {
		this.instantiator = instantiator;
	}

	/**
	 * instancia uma nova entidade injetando o id no objeto criado, o objeto já
	 * possui as dependencias inicializadas pelo container
	 */

	@Override
	@SuppressWarnings("rawtypes")
	public Object instantiate(String className, EntityMode mode, Serializable id) {
		// instancia o objeto pelo container com as dependencias
		Object object = instantiator.instantiate(new Target(new Mirror().reflectClass(className), ""), new Parameters());
		// procura o field que tem a anotação id
		Field field = new Mirror().on(className).reflectAll().fieldsMatching(new Matcher<Field>() {
			/**
			 * aceita apenas o field com /@id
			 * 
			 * @param field
			 * @return
			 */
			public boolean accepts(Field field) {
				return field.isAnnotationPresent(Id.class);
			}
		}).get(0);

		// seta o valor do id no field
		new Mirror().on(object).set().field(field).withValue(id);

		return object;
	}

}