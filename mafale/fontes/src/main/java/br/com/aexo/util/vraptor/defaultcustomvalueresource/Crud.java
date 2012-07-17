package br.com.aexo.util.vraptor.defaultcustomvalueresource;

import org.hibernate.criterion.Order;

import br.com.aexo.util.dominio.Entidade;

public class Crud {

	private final String uriBase;
	private final Class<? extends Entidade> entidade;
	private final Order ordenacaoDefault;

	public Crud(String uriBase, Class<? extends Entidade> entidade, Order ordenacaoDefault) {
		super();
		this.uriBase = uriBase;
		this.entidade = entidade;
		this.ordenacaoDefault = ordenacaoDefault;
	}

	public String getUriBase() {
		return uriBase;
	}

	public Class<? extends Entidade> getEntidade() {
		return entidade;
	}

	public Order getOrdenacaoDefault() {
		return ordenacaoDefault;
	}

}
