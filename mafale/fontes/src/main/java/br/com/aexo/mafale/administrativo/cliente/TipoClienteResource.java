package br.com.aexo.mafale.administrativo.cliente;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.aexo.util.exceptions.DominioException;
import br.com.aexo.util.hibernate.Load;
import br.com.aexo.util.resources.DefaultResource;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

@Resource
public class TipoClienteResource extends DefaultResource<TipoCliente> {

	private final Result result;

	public TipoClienteResource(Session session, Result result,
			Validator validator) {
		super(TipoCliente.class, session, result, validator);
		this.result = result;
	}

	@Delete("/data/tiposcliente/{tipoCliente.id}")
	public void remove(@Load TipoCliente tipoCliente) {
		try {
			tipoCliente.remover();
			result.use(Results.status()).ok();
		} catch(DominioException e){
			result.use(Results.status()).badRequest(e.getMessage());
		}
	}

	@Post({ "/data/tiposcliente/{tipoCliente.id}", "/data/tiposcliente" })
	@Consumes
	public void salvar(TipoCliente tipoCliente) {
		if (!valido(tipoCliente))
			return;

		if (tipoCliente.getId() == null) {
			tipoCliente.salvar();
			super.salvar(tipoCliente);
			return;
		}

		TipoCliente db = tipoCliente.carregar();
		db.setDescricao(tipoCliente.getDescricao());
		db.salvar();
		super.salvar(tipoCliente);
	}

	@Get("/data/tiposcliente")
	public void listar(Integer inicio, Integer numRegistros) {
		super.listar(inicio, numRegistros);
	}

	@Get("/data/tiposcliente/{tipoCliente.id}")
	public void recuperar(@Load TipoCliente tipoCliente) {
		super.recuperar(tipoCliente);
	}

	@Override
	public Order ordenarPor() {
		return Order.asc("descricao");
	}

}
