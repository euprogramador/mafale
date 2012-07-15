package br.com.aexo.mafale.administrativo;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import br.com.aexo.util.ajax.Data;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

public abstract class DefaultResource<E> {

	private Session session;
	private Result result;
	private Validator validator;
	private Class<E> classe;

	public DefaultResource(Class<E> classe, Session session, Result result,
			Validator validator) {
		this.session = session;
		this.result = result;
		this.validator = validator;
		this.classe = classe;
	}

	public abstract Order ordenarPor();

	@SuppressWarnings("unchecked")
	public void listar(Integer inicio, Integer numRegistros) {
		Data<E> data = new Data<E>();

		Criteria criteria = session.createCriteria(classe);
		criteria.addOrder(ordenarPor());

		if (inicio != null && inicio != 0)
			criteria.setFirstResult(inicio);

		if (numRegistros != null && numRegistros != 0)
			criteria.setMaxResults(numRegistros);

		data.setListagem(criteria.list());

		criteria = session.createCriteria(TipoCliente.class);
		criteria.setProjection(Projections.rowCount());
		data.setContagem((Long) criteria.uniqueResult());

		data.setInicio(inicio);
		data.setNumRegistros(numRegistros);

		result.use(Results.json()).withoutRoot().from(data).include("listagem")
				.serialize();
	}

	public void recuperar(TipoCliente tipoCliente) {
		result.use(Results.json()).withoutRoot().from(tipoCliente).serialize();
	}

	public void salvar(E entidade) {
		validator.validate(entidade);
		validator.onErrorSendBadRequest();
		if (validator.hasErrors())
			return;

		session.saveOrUpdate(entidade);
		result.use(Results.json()).withoutRoot().from(entidade).serialize();
	}

	public void remove(E entidade) {
		session.delete(entidade);
		result.use(Results.status()).ok();
	}

}
