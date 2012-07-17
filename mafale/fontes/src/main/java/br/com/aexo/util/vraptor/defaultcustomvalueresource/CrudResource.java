package br.com.aexo.util.vraptor.defaultcustomvalueresource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import br.com.aexo.util.ajax.Data;
import br.com.aexo.util.dominio.Entidade;
import br.com.aexo.util.exceptions.DominioException;
import br.com.aexo.util.vraptor.view.Json;
import br.com.aexo.util.vraptor.view.Status;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

public class CrudResource<E extends Entidade> {

	private final Result result;
	private final Validator validator;
	private final Session session;
	private final Class<? extends Entidade> classe;

	public CrudResource(Session session, Result result, Validator validator, Class<? extends Entidade> classe) {
		this.session = session;
		this.result = result;
		this.validator = validator;
		this.classe = classe;
	}

	@SuppressWarnings("unchecked")
	public void remover(E entidade) {
		entidade = (E) entidade.carregar();
		if (entidade==null){
			result.notFound();
			return;
		}
		
		try {
			entidade.remover();
			result.use(Results.status()).ok();
		} catch (DominioException e) {
			result.use(Status.class).badRequest(e.getMessage());
		}
	}


	public void salvar(E entidade) {
		try {
			validator.validate(entidade);
			validator.onErrorSendBadRequest();
			if (validator.hasErrors())
				return;

			if (entidade.getId() == null) {
				entidade.salvar();
				result.use(Results.json()).withoutRoot().from(entidade).serialize();
				return;
			}

			Entidade db = entidade.carregar();
			db.preencherCom(entidade);
			db.salvar();
			result.use(Results.json()).withoutRoot().from(db).serialize();
		} catch (DominioException e) {
			result.use(Status.class).badRequest(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void listar(Order order, Integer inicio, Integer numRegistros) {
		Data<E> data = new Data<E>();

		Criteria criteria = session.createCriteria(classe);
		criteria.addOrder(order);

		if (inicio != null && inicio != 0)
			criteria.setFirstResult(inicio);

		if (numRegistros != null && numRegistros != 0)
			criteria.setMaxResults(numRegistros);

		data.setListagem(criteria.list());

		criteria = session.createCriteria(classe);
		criteria.setProjection(Projections.rowCount());
		data.setContagem((Long) criteria.uniqueResult());

		data.setInicio(inicio);
		data.setNumRegistros(numRegistros);

		result.use(Json.class).withoutRoot().from(data).include("listagem").serialize();
	}

	@SuppressWarnings("unchecked")
	public void recuperar(Long id) {
		E entidade = (E) session.get(classe, id);
		if (entidade == null) {
			result.notFound();
			return;
		}
		result.use(Json.class).withoutRoot().from(entidade).serialize();
	}

}
