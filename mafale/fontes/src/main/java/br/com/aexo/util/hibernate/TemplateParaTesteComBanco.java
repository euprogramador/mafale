package br.com.aexo.util.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.com.aexo.util.exceptions.InfraestruturaException;

/**
 * Classe utilitária para fazer teste de integração com o banco de dados em
 * memoria
 * 
 * @author carlosr
 * 
 */
public class TemplateParaTesteComBanco {

	private SessionFactory sessionFactory;
	private Session session;

	/**
	 * configura o banco de acordo com os dados passados
	 * 
	 * @param entidades
	 */
	public void configurarBanco(List<Class<?>> entidades) {
		Configuration config = new Configuration();

		config.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

		config.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		config.setProperty("hibernate.connection.url", "jdbc:h2:mem:aexo");
		config.setProperty("hibernate.connection.username", "sa");
		config.setProperty("hibernate.connection.password", "");
		config.setProperty("hibernate.hbm2ddl.auto", "create");
		config.setProperty("hibernate.show_sql", "true");
		config.setProperty("hibernate.validator.autoregister_listeners","false"); 
		config.setProperty("javax.persistence.validation.mode","none");
		

		for (Class<?> clazz : entidades) {
			config.addAnnotatedClass(clazz);
		}
		sessionFactory = config.buildSessionFactory();
		session = sessionFactory.openSession();
	}

	/**
	 * recupera uma session
	 * 
	 * @return
	 */
	public Session getSession() {
		if (session == null) {
			throw new InfraestruturaException("Você deve primeiro invocar configurarBanco com a lista de entidades a serem testadas");
		}

		return new SessionProxy(session);
	}

	/**
	 * fecha a conexão com o banco
	 */

	public void fecharBanco() {
		if (session != null) {
			session.close();
		}
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

}
