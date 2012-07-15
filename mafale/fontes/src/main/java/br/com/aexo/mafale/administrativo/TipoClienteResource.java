package br.com.aexo.mafale.administrativo;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import br.com.aexo.util.ajax.Data;
import br.com.aexo.util.hibernate.Load;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

@Resource
public class TipoClienteResource {

	private final Session session;
	private final Result result;
	private final Validator validator;

	public TipoClienteResource(Session session, Result result,Validator validator) {
		this.session = session;
		this.result = result;
		this.validator = validator;
	}

	@Delete("/data/tiposcliente/{tipoCliente.id}")
	public void remove(@Load TipoCliente tipoCliente){
		session.delete(tipoCliente);
		result.use(Results.status()).ok();
	}
	
	@Post({"/data/tiposcliente/{tipoCliente.id}","/data/tiposcliente"})
	@Consumes
	public void salvar(TipoCliente tipoCliente){
		validator.validate(tipoCliente);
		if (validator.hasErrors()){
			result.use(Results.status()).badRequest(validator.getErrors());
			return;
		}
		session.saveOrUpdate(tipoCliente);
		result.use(Results.json()).withoutRoot().from(tipoCliente).serialize();
	}
	
	@Get("/data/tiposcliente")
	@SuppressWarnings("unchecked")
	public void list(Integer inicio,Integer numRegistros) {
		Data<TipoCliente> data = new Data<TipoCliente>();
		
		Criteria criteria = session.createCriteria(TipoCliente.class);
		criteria.addOrder(Order.asc("id"));
		
		if (inicio!= null && inicio!=0)
			criteria.setFirstResult(inicio);
		
		if (numRegistros!=null && numRegistros!= 0)
			criteria.setMaxResults(numRegistros);
		
		data.setListagem(criteria.list());
		
		criteria  = session.createCriteria(TipoCliente.class);
		criteria.setProjection(Projections.rowCount());
		data.setContagem((Long)criteria.uniqueResult());
		
		data.setInicio(inicio);
		data.setNumRegistros(numRegistros);
		
		result.use(Results.json()).withoutRoot().from(data).include("listagem").serialize();
	}

	@Get("/data/tiposcliente/{tipoCliente.id}")
	public void recuperar(@Load TipoCliente tipoCliente){
		result.use(Results.json()).withoutRoot().from(tipoCliente).serialize();
	}
	
}

