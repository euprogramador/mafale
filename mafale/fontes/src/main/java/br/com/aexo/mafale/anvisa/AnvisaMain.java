package br.com.aexo.mafale.anvisa;

import br.com.aexo.mafale.anvisa.procedimentoparaconsulta.PassoExecucaoDaConsulta;
import br.com.aexo.mafale.anvisa.procedimentoparaconsulta.PassoInterpretacaoDoResultadoDaConsulta;
import br.com.aexo.mafale.anvisa.procedimentoparaconsulta.ProcedimentoParaConsultaNaAnvisa;

public class AnvisaMain {

	public static void main(String[] args) {

		System.out.println("processo cnpj");

		ProcessoNaAnvisa processo = new ProcessoNaAnvisa();
		
		String processoInformado = args[0];
		String cnpj = args[1];

		processo.setProcesso(processoInformado);
		processo.setCnpj(cnpj);

		PassoExecucaoDaConsulta passoExecucaoDaConsulta = new PassoExecucaoDaConsulta();
		PassoInterpretacaoDoResultadoDaConsulta passoInterpretacaoDoResultadoDaConsulta = new PassoInterpretacaoDoResultadoDaConsulta();
		ProcedimentoParaConsultaNaAnvisa procedimento = new ProcedimentoParaConsultaNaAnvisa(passoExecucaoDaConsulta, passoInterpretacaoDoResultadoDaConsulta);

		Anvisa anvisa = new Anvisa(procedimento);
		anvisa.consultar(processo);

		for (PeticaoNaAnvisa peticao : processo.getPeticoes()) {
			PeticaoNaAnvisa situacaoNaAnvisa = peticao;
			System.out.println("--------------------------------------------------------------------------------------");
			System.out.println("Expediente		: " + situacaoNaAnvisa.getExpediente());
			System.out.println("Data Entrada		: " + situacaoNaAnvisa.getDataEntrada());
			System.out.println("Protocolo		: " + situacaoNaAnvisa.getProtocolo());
			System.out.println("Assunto			: " + situacaoNaAnvisa.getAssunto());
			System.out.println("Situação		: " + situacaoNaAnvisa.getSituacao());
			System.out.println("Encontra-se		: " + situacaoNaAnvisa.getEncontraSeNa());
			System.out.println("Encontra-se data	: " + situacaoNaAnvisa.getEncontraSeDesde());
			System.out.println("Publicado Em		: " + situacaoNaAnvisa.getDataPublicacao());
			System.out.println("Resolução		: " + situacaoNaAnvisa.getResolucao());
		}
	}

}
