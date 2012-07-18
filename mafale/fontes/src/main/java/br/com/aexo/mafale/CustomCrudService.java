package br.com.aexo.mafale;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import org.hibernate.criterion.Order;

import br.com.aexo.mafale.administrativo.cliente.Cliente;
import br.com.aexo.mafale.administrativo.cliente.PorteCliente;
import br.com.aexo.mafale.administrativo.cliente.TipoCliente;
import br.com.aexo.mafale.administrativo.servicos.Assunto;
import br.com.aexo.mafale.administrativo.servicos.Historico;
import br.com.aexo.mafale.administrativo.servicos.Peticao;
import br.com.aexo.mafale.administrativo.servicos.Servico;
import br.com.aexo.mafale.administrativo.servicos.TipoServico;
import br.com.aexo.util.vraptor.defaultcustomvalueresource.Crud;
import br.com.aexo.util.vraptor.defaultcustomvalueresource.CrudService;
import br.com.caelum.vraptor.ioc.ApplicationScoped;

@ApplicationScoped
public class CustomCrudService extends CrudService {

	public CustomCrudService(ServletContext servletContext) {
		super(servletContext);
	}

	public List<Crud> getCruds() {
		return Arrays.asList( //
				new Crud("/data/tiposcliente", TipoCliente.class, Order.asc("descricao")),//
				new Crud("/data/portescliente", PorteCliente.class, Order.asc("descricao")),//
				new Crud("/data/clientes", Cliente.class, Order.asc("razaoSocial")),//
				new Crud("/data/tiposservico", TipoServico.class, Order.asc("descricao")),//
				new Crud("/data/assuntospeticao", Assunto.class, Order.asc("descricao")),//
				new Crud("/data/servicos", Servico.class, Order.asc("id")),//
				new Crud("/data/peticoes", Peticao.class, Order.asc("id")),//
				new Crud("/data/historicos", Historico.class, Order.asc("id"))//
				);
	}
}
