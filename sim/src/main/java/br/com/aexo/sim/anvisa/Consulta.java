package br.com.aexo.sim.anvisa;

import br.com.aexo.sim.servicos.Servico;

public class Consulta {

	private Servico servico;
	private boolean resultado;

	public Consulta(Servico servico, boolean resultado) {
		super();
		this.servico = servico;
		this.resultado = resultado;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public boolean isResultado() {
		return resultado;
	}

	public void setResultado(boolean resultado) {
		this.resultado = resultado;
	}

}
