package br.com.aexo.util.vraptor.defaultcustomvalueresource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import br.com.aexo.mafale.administrativo.cliente.TipoCliente;
import br.com.aexo.util.ajax.Data;
import br.com.aexo.util.dominio.Entidade;
import br.com.aexo.util.exceptions.DominioException;
import br.com.aexo.util.hibernate.Load;
import br.com.aexo.util.vraptor.view.Json;
import br.com.aexo.util.vraptor.view.Status;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.view.Results;

@Component
public class CrudResource<E extends Entidade> {

	private final Result result;
	private final Validator validator;
	private final Session session;

	public CrudResource(Session session, Result result, Validator validator) {
		this.session = session;
		this.result = result;
		this.validator = validator;
	}

	public void remove(@Load E entity) {
		try {
			entity.remover();
			result.use(Results.status()).ok();
		} catch (DominioException e) {
			result.use(Status.class).badRequest(e.getMessage());
		}
	}

	@Consumes
	public void salvar(E entity) {
		validator.validate(entity);
		validator.onErrorSendBadRequest();
		if (validator.hasErrors())
			return;

		if (entity.getId() == null) {
			entity.salvar();
			result.use(Results.json()).withoutRoot().from(entity).serialize();
			return;
		}

		Entidade db = entity.carregar();
		db.salvar();
		result.use(Results.json()).withoutRoot().from(db).serialize();
	}

	@SuppressWarnings("unchecked")
	public void listar(String classe,Order order,Integer inicio, Integer numRegistros) {
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

	public void recuperar(@Load TipoCliente tipoCliente) {
		result.use(Results.json()).withoutRoot().from(tipoCliente).serialize();
	}

}
