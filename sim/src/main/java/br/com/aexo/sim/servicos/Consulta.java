package br.com.aexo.sim.servicos;

import java.io.Serializable;

public class Consulta implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Servico servico;
	private final ResultadoConsulta resultadoConsulta;
	private Integer delay = 0;

	public Consulta(Servico servico, ResultadoConsulta resultadoConsulta) {
		this.servico = servico;
		this.resultadoConsulta = resultadoConsulta;
	}

	public Servico getServico() {
		return servico;
	}

	public ResultadoConsulta getResultadoConsulta() {
		return resultadoConsulta;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

}
