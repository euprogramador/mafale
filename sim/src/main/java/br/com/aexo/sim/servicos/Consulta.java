package br.com.aexo.sim.servicos;

public class Consulta {

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
