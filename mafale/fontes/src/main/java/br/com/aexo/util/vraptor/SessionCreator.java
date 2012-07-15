package br.com.aexo.util.vraptor;

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleStateException;

import br.com.aexo.util.exceptions.EstadoInvalidoDeEntidadeException;
import br.com.aexo.util.hibernate.interceptors.HibernateInterceptors;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.proxy.MethodInvocation;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.proxy.SuperMethod;

/**
 * Responsavel por prover para cada request uma session
 * 
 * @author carlosr
 * 
 */
@RequestScoped
@Component
public class SessionCreator implements ComponentFactory<Session> {

	private final SessionFactory factory;
	private Session session;
	private Session lazySession;
	private final Proxifier proxifier;
	private final HibernateInterceptors hibernateInterceptors;

	/**
	 * cria uma session creator
	 * 
	 * @param factory
	 * @param hibernateInterceptors
	 * @param proxifier
	 */
	public SessionCreator(SessionFactory factory, HibernateInterceptors hibernateInterceptors, Proxifier proxifier) {
		this.factory = factory;
		this.hibernateInterceptors = hibernateInterceptors;
		this.proxifier = proxifier;
	}

	/**
	 * monta a session 
	 */
	@PostConstruct
	public void create() {
		this.session = proxifier.proxify(Session.class, new MethodInvocation<Session>() {
			
			public Object intercept(Session proxy, Method method, Object[] args, SuperMethod superMethod) {
				try {
					Object resultInvocation = new Mirror().on(getLazySession()).invoke().method(method).withArgs(args);
					return resultInvocation;
				} catch(Exception e){
					Throwable rootCause = getRootCause(e);
					if (rootCause instanceof StaleStateException) {
						session.getTransaction().rollback();
						throw new EstadoInvalidoDeEntidadeException();
					} else { 
						throw new RuntimeException(e);
					}
				}
				
			}

			private Throwable getRootCause(Throwable e) {
				if (e.getCause()==null)
					return e;
				else 
					return getRootCause(e.getCause());
			}
		});
	}

	/**
	 * recupera uma session lazy
	 * 
	 * @return
	 */
	protected Session getLazySession() {
		if (lazySession == null) {
			lazySession = factory.openSession(hibernateInterceptors);
		}
		return lazySession;
	}

	/**
	 * recupera a session criada
	 */
	public Session getInstance() {
		return session;
	}

	/**
	 * fecha a conexão
	 */
	@PreDestroy
	public void destroy() {
		if (lazySession != null) {
			this.lazySession.close();
			this.lazySession = null;
		}
	}

}