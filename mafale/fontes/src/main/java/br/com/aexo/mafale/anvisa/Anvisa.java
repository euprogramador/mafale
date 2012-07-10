package br.com.aexo.mafale.anvisa;

import br.com.aexo.Contexto;
import br.com.aexo.Procedimento;

public class Anvisa {

	private final ProcedimentoParaConsultaNaAnvisa procedimentoParaConsultaNaAnvisa;

	public Anvisa(ProcedimentoParaConsultaNaAnvisa procedimentoParaConsultaNaAnvisa) {
		this.procedimentoParaConsultaNaAnvisa = procedimentoParaConsultaNaAnvisa;
	}

	public void consultar(ProcessoNaAnvisa processo) {
		Contexto contexto = new Contexto();
		contexto.set("processo", processo);
		procedimentoParaConsultaNaAnvisa.executar(contexto);
		
		public void consultar(Servico servico) {
			Contexto contexto = new Contexto();
			contexto.set("servico", servico);
			processoConsulta.executar(contexto);
			
			SituacaoNaAnvisa situacaoNaAnvisa = contexto.get("situacaoNaAnvisa");
			System.out.println("Expediente		: "+situacaoNaAnvisa.getExpediente());
			System.out.println("Data Entrada		: "+formata(situacaoNaAnvisa.getDataEntrada()));
			System.out.println("Protocolo		: "+situacaoNaAnvisa.getProtocolo());
			System.out.println("Assunto			: "+situacaoNaAnvisa.getAssunto());
			System.out.println("Situação		: "+situacaoNaAnvisa.getSituacao());
			System.out.println("Encontra-se		: "+situacaoNaAnvisa.getEncontraSeNa());
			System.out.println("Encontra-se data	: "+formata(situacaoNaAnvisa.getEncontraSeDesde()));
			System.out.println("Publicado Em		: "+formata(situacaoNaAnvisa.getDataPublicacao()));
			System.out.println("Resolução		: "+situacaoNaAnvisa.getResolucao());
		}

		private String formata(Date date) {
			if (date ==null)
				return "";
			else
				return new SimpleDateFormat("dd/MM/yyyy").format(date);
		}
		
	}

}
