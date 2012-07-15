package br.com.aexo.util.vraptor;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.ComponentFactory;

/**
 * Cria uma session factory com base no hibernate.cfg.xml e mapea as entidades
 * encontradas no classpath
 * 
 * @author carlosr
 * 
 */
@ApplicationScoped
public class SessionFactoryCreator implements ComponentFactory<SessionFactory> {

	private final Configuration configuration;
	private SessionFactory factory;

	/**
	 * inicia uma configuração de session factory.
	 * 
	 * @param entityScanner
	 * @param context
	 */
	public SessionFactoryCreator(EntityScanner entityScanner, ServletContext context) {
		this.configuration = new Configuration().configure(new File(context.getRealPath("/WEB-INF/classes/hibernate.cfg.xml")));
		List<Class<?>> scan = entityScanner.scan();

		for (Class<?> classe : scan) {
			configuration.addAnnotatedClass(classe);
		}
	}

	/**
	 * cria uma session factory.
	 */
	@PostConstruct
	public void create() {
		factory = configuration.buildSessionFactory();
	}

	/**
	 * recupera a instancia do session factory.
	 */
	public SessionFactory getInstance() {
		return factory;
	}

	/**
	 * ao destruir o bean a factory deve ser fechada.
	 */
	@PreDestroy
	public void destroy() {
		factory.close();
	}

}
