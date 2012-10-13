package br.com.aexo.sim.anvisa;

import br.com.aexo.sim.anvisa.procedimentoparaconsulta.ProcedimentoParaConsultaNaAnvisa;
import br.com.aexo.sim.core.processos.Contexto;


/**
 * interface que executa o procedimento de consulta na anvisa coletando dados
 * das petições junto ao site da anvisa
 * 
 * @author carlosr
 * 
 */
public class Anvisa {

	private final ProcedimentoParaConsultaNaAnvisa procedimentoParaConsultaNaAnvisa;

	public Anvisa(ProcedimentoParaConsultaNaAnvisa procedimentoParaConsultaNaAnvisa) {
		this.procedimentoParaConsultaNaAnvisa = procedimentoParaConsultaNaAnvisa;
	}

	public void consultar(ProcessoNaAnvisa processo) {
		Contexto contexto = new Contexto();
		contexto.set("processo", processo);
		procedimentoParaConsultaNaAnvisa.executar(contexto);
	}

}
