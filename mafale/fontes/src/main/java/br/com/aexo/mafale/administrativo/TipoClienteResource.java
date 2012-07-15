package br.com.aexo.mafale.administrativo;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.aexo.util.hibernate.Load;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;

@Resource
public class TipoClienteResource extends DefaultResource<TipoCliente> {

	public TipoClienteResource(Session session, Result result,
			Validator validator) {
		super(TipoCliente.class, session, result, validator);
	}

	@Delete("/data/tiposcliente/{tipoCliente.id}")
	public void remove(@Load TipoCliente tipoCliente) {
		super.remove(tipoCliente);
	}

	@Post({ "/data/tiposcliente/{tipoCliente.id}", "/data/tiposcliente" })
	@Consumes
	public void salvar(TipoCliente tipoCliente) {
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
