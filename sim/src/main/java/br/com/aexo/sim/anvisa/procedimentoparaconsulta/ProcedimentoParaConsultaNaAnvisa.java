package br.com.aexo.sim.anvisa.procedimentoparaconsulta;

import br.com.aexo.sim.core.processos.Procedimento;

public class ProcedimentoParaConsultaNaAnvisa extends Procedimento {

	public ProcedimentoParaConsultaNaAnvisa(PassoExecucaoDaConsulta passoExecucaoDaConsulta, PassoInterpretacaoDoResultadoDaConsulta passoInterpretacaoDoResultadoDaConsulta) {
		super(passoExecucaoDaConsulta,passoInterpretacaoDoResultadoDaConsulta);
	}

}
