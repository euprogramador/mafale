package br.com.aexo.util.repositorio;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.aexo.util.exceptions.DominioException;

/**
 * template de repositorio
 * 
 * @author carlosr
 * 
 * @param <T>
 */
public class Repositorio<T> {

	private final Class<? extends T> clazz;
	private final Session session;
	private final Order ordemDefault;

	public Repositorio(Session session, Class<? extends T> clazz) {
		this(session, clazz, null);
	}

	public Repositorio(Session session, Class<? extends T> clazz, Order ordemDefault) {
		this.session = session;
		this.clazz = clazz;
		this.ordemDefault = ordemDefault;
	}

	@SuppressWarnings("unchecked")
	public T carregar(long id) {
		Object object = session.get(clazz, id);

		if (object == null)
			throw new DominioException("Tentativa de carregar registro inexistente, possivelmente foi excluído por outro usuário");

		return (T) object;
	}

	public void remover(T entity) {
		session.delete(entity);
	}

	public void salvar(T entity) {
		session.saveOrUpdate(entity);
	}

	protected Session getSession(){
		return session;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> listar() {
		Criteria criteria = session.createCriteria(clazz);

		if (ordemDefault != null)
			criteria.addOrder(ordemDefault);

		return criteria.list();
	}

}
