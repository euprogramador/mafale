package br.com.aexo.mafale.administrativo.cliente;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.aexo.util.hibernate.Load;
import br.com.aexo.util.resources.DefaultResource;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;

@Resource
public class PorteClienteResource extends DefaultResource<PorteCliente> {

	public PorteClienteResource(Session session, Result result,
			Validator validator) {
		super(PorteCliente.class, session, result, validator);
	}

	@Delete("/data/portescliente/{porteCliente.id}")
	public void remove(@Load PorteCliente porteCliente) {
		super.remove(porteCliente);
	}

	@Post({ "/data/portescliente/{porteCliente.id}", "/data/portescliente" })
	@Consumes
	public void salvar(PorteCliente porteCliente) {
		if (!valido(porteCliente))
			return;

		if (porteCliente.getId() == null) {
			porteCliente.salvar();
			super.salvar(porteCliente);
			return;
		}

		PorteCliente db = porteCliente.carregar();
		db.setDescricao(porteCliente.getDescricao());
		db.salvar();
		super.salvar(porteCliente);
	}

	@Get("/data/portescliente")
	public void listar(Integer inicio, Integer numRegistros) {
		super.listar(inicio, numRegistros);
	}

	@Get("/data/portescliente/{porteCliente.id}")
	public void recuperar(@Load PorteCliente porteCliente) {
		super.recuperar(porteCliente);
	}

	@Override
	public Order ordenarPor() {
		return Order.asc("descricao");
	}

}
