package br.com.aexo.mafale.anvisa;

import br.com.aexo.Contexto;

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
