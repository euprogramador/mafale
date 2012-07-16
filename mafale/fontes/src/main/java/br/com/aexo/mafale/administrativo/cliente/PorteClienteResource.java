package br.com.aexo.mafale.administrativo.cliente;

import java.util.Arrays;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.aexo.util.exceptions.DominioException;
import br.com.aexo.util.hibernate.Load;
import br.com.aexo.util.resources.DefaultResource;
import br.com.aexo.util.vraptor.view.Status;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

@Resource
public class PorteClienteResource extends DefaultResource<PorteCliente> {

	private final Result result;

	public PorteClienteResource(Session session, Result result,
			Validator validator) {
		super(PorteCliente.class, session, result, validator);
		this.result = result;
	}

	@Delete("/data/portescliente/{porteCliente.id}")
	public void remove(@Load PorteCliente porteCliente) {
		try {
			porteCliente.remover();
			result.use(Results.status()).ok();
		} catch(DominioException e){
			result.use(Status.class).badRequest(e.getMessage());
		}
	}

	@Post({ "/data/portescliente/{porteCliente.id}", "/data/portescliente" })
	@Consumes
	public void salvar(PorteCliente porteCliente) {
		if (!valido(porteCliente))
			return;

		if (porteCliente.getId() == null) {
			porteCliente.salvar();
			result.use(Results.json()).withoutRoot().from(porteCliente).serialize();
			return;
		}

		PorteCliente db = porteCliente.carregar();
		db.setDescricao(porteCliente.getDescricao());
		db.salvar();
		result.use(Results.json()).withoutRoot().from(porteCliente).serialize();
	}

	@Get("/data/portescliente")
	public void listar(Integer inicio, Integer numRegistros) {
		super.listar(inicio, numRegistros);
	}

	@Get("/data/portescliente/{porteCliente.id}")
	public void recuperar(@Load PorteCliente porteCliente) {
		result.use(Results.json()).withoutRoot().from(porteCliente).serialize();
	}

	@Override
	public Order ordenarPor() {
		return Order.asc("descricao");
	}

}
