package br.com.aexo.mafale;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;

import br.com.aexo.util.hibernate.interceptors.DependencyInjectionInterceptor;
import br.com.aexo.util.hibernate.interceptors.HibernateInterceptors;

/**
 * classe responsavel por representar um criar um delegate para o interceptor do
 * hibernate
 * 
 * @author carlosr
 * 
 */
public class MafaleHibernateInterceptors extends EmptyInterceptor implements HibernateInterceptors {

	private static final long serialVersionUID = 1L;

	private DependencyInjectionInterceptor dependencyInjectionInterceptor;


	public MafaleHibernateInterceptors(DependencyInjectionInterceptor dependencyInjectionInterceptor) {
		this.dependencyInjectionInterceptor = dependencyInjectionInterceptor;
	}

	/**
	 * instancia a entidade via dependency injection
	 */
	@Override
	public Object instantiate(String entityName, EntityMode entityMode, Serializable id) {
		return dependencyInjectionInterceptor.instantiate(entityName, entityMode, id);
	}


}
