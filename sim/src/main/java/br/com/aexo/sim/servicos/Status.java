package br.com.aexo.sim.servicos;

public enum Status {

	PENDENTE("Pendente"), //
	EM_TRAMITACAO("Em tramitação"), //
	CONCLUIDO_COM_RENOVACAO("Concluído com renovação"), //
	CONCLUIDO_SEM_RENOVACAO("Concluído sem renovação");//

	private final String descricao;

	private Status(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
