package br.com.aexo.mafale.anvisa.procedimentoparaconsulta;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import br.com.aexo.mafale.anvisa.PeticaoNaAnvisa;
import br.com.aexo.mafale.anvisa.ProcessoNaAnvisa;
import br.com.aexo.mafale.anvisa.procedimentoparaconsulta.PassoInterpretacaoDoResultadoDaConsulta;
import br.com.aexo.util.procedimentos.Contexto;

public class ProcedimentoParaIntepretacaoDaConsultaTest {

	PassoInterpretacaoDoResultadoDaConsulta passo;

	@Before
	public void setup() {
		passo = new PassoInterpretacaoDoResultadoDaConsulta();
	} 


	@Test
	public void deveriaCarregarPeticaoRaiz() throws Exception {

		Contexto contexto = new Contexto();
		ProcessoNaAnvisa processo = new ProcessoNaAnvisa();
		contexto.set("processo", processo);
		contexto.set("consulta", recuperarArquivo("resultadoConsultaComApenasPeticaoRaiz"));
		
		passo.executar(contexto);
		
		List<PeticaoNaAnvisa> peticoes = processo.getPeticoes();

		assertThat(peticoes.size(), is(1));
		assertThat(peticoes.get(0).getExpediente(), is("352447/05-7"));
		assertThat(peticoes.get(0).getDataEntrada(), is(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2005"))));
		assertThat(peticoes.get(0).getProtocolo(), is("25352.289845/2005-98"));
		assertThat(peticoes.get(0).getAssunto(), is("702 - MEDICAMENTOS e INSUMOS FARMACÊUTICOS - (AFE) de DISTRIBUIDORA do produto"));
		assertThat(peticoes.get(0).getSituacao(), is("Publicado deferimento em 26/09/2005"));
		assertThat(peticoes.get(0).getEncontraSeNa(), is("Encaminhado para COAFE"));
		assertThat(peticoes.get(0).getEncontraSeDesde(),is(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse("28/06/2012"))));
		assertThat(peticoes.get(0).getDataPublicacao(),is(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse("26/09/2005"))));
		assertThat(peticoes.get(0).getResolucao(), is("2367"));
	}

	
	
	@Test
	public void deveriaCarregarPeticaoRaizJuntoComOutrasPeticoes() throws Exception {

		Contexto contexto = new Contexto();
		ProcessoNaAnvisa processo = new ProcessoNaAnvisa();
		contexto.set("processo", processo);
		contexto.set("consulta", recuperarArquivo("resultadoConsultaComPeticoesExtras"));

		passo.executar(contexto);

		List<PeticaoNaAnvisa> peticoes = processo.getPeticoes();
		
		assertThat(peticoes.size(), is(11));
		assertThat(peticoes.get(0).getExpediente(), is("352447/05-7"));
		assertThat(peticoes.get(0).getDataEntrada(), is(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2005"))));
		assertThat(peticoes.get(0).getProtocolo(), is("25352.289845/2005-98"));
		assertThat(peticoes.get(0).getAssunto(), is("702 - MEDICAMENTOS e INSUMOS FARMACÊUTICOS - (AFE) de DISTRIBUIDORA do produto"));
		assertThat(peticoes.get(0).getSituacao(), is("Publicado deferimento em 26/09/2005"));
		assertThat(peticoes.get(0).getEncontraSeNa(), is("Encaminhado para COAFE"));
		assertThat(peticoes.get(0).getEncontraSeDesde(),is(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse("28/06/2012"))));
		assertThat(peticoes.get(0).getDataPublicacao(),is(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse("26/09/2005"))));
		assertThat(peticoes.get(0).getResolucao(), is("2367"));
		
		assertThat(peticoes.get(1).getExpediente(), is("544834/08-4"));
		assertThat(peticoes.get(1).getDataEntrada(), is(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse("13/06/2008"))));
		assertThat(peticoes.get(1).getProtocolo(), is("25352.527809/2008-81"));
		assertThat(peticoes.get(1).getAssunto(), is("7157 - MEDICAMENTOS E INSUMOS FARMACÊUTICOS - (Alteração na AFE) de DISTRIBUIDORA do produto - RESPONSÁVEL TÉCNICO"));
		assertThat(peticoes.get(1).getSituacao(), is("Em análise"));
		assertThat(peticoes.get(1).getEncontraSeNa(), is("Encaminhado para GIMED"));
		assertThat(peticoes.get(1).getEncontraSeDesde(),is(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse("17/09/2010"))));
		assertThat(peticoes.get(1).getDataPublicacao(),is(nullValue()));
		assertThat(peticoes.get(1).getResolucao(), is(nullValue()));
		
		assertThat(peticoes.get(2).getExpediente(), is("552459/08-8"));
		assertThat(peticoes.get(2).getDataEntrada(), is(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse("16/06/2008"))));
		assertThat(peticoes.get(2).getProtocolo(), is("25352.527776/2008-79"));
		assertThat(peticoes.get(2).getAssunto(), is("7157 - MEDICAMENTOS E INSUMOS FARMACÊUTICOS - (Alteração na AFE) de DISTRIBUIDORA do produto - RESPONSÁVEL TÉCNICO"));
		assertThat(peticoes.get(2).getSituacao(), is("Protocolizado"));
		assertThat(peticoes.get(2).getEncontraSeNa(), is("Encontra-se na GIMED"));
		assertThat(peticoes.get(2).getEncontraSeDesde(),is(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse("07/08/2008"))));
		assertThat(peticoes.get(2).getDataPublicacao(),is(nullValue()));
		assertThat(peticoes.get(2).getResolucao(), is(nullValue()));

	}

	public String recuperarArquivo(String arquivo) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(arquivo)));
		String linha = null;
		StringBuffer sb = new StringBuffer();
		try {
			while ((linha = reader.readLine()) != null) {
				sb.append(linha).append("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}