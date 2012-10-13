package br.com.aexo.sim.core.persistencia;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.hibernate.Session;
import org.jboss.solder.core.ExtensionManaged;

public class EntityManagerProducer {

	@ExtensionManaged
	@Produces
	@PersistenceUnit
	@ConversationScoped
	EntityManagerFactory producerField;

	@Produces
	@EntityManagerRepository
	@PersistenceContext
	EntityManager entityManager;

	@Produces
	@ConversationScoped
	public Session recuperarSession(EntityManager entityManager){
		return entityManager.unwrap(Session.class);
	}
	
}