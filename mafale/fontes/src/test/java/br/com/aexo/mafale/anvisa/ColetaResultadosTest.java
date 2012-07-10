package br.com.aexo.mafale.anvisa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;

public class ColetaResultadosTest {

	private ColetaResultados coletor;

	@Before
	public void setup() {
		coletor = new ColetaResultados();
	}

	@Test
	public void aoExecutarDeveriaParsearOHTMLERecuperarResultadosDaConsulta() throws Exception {
		Servico servico = new Servico();
		servico.setProcesso("25351.297487/2005-05");
		servico.setExpediente("0318836/12-1");
		
		Contexto contexto = new Contexto();
		contexto.set("servico", servico);
		contexto.set("consulta", recuperarArquivo("resultadoconsulta") );
		coletor.executar(contexto); 
		
		SituacaoNaAnvisa situacao =  contexto.get("situacaoNaAnvisa");
		
		assertThat(situacao, is(notNullValue()));
		assertThat(situacao.getExpediente(), is(servico.getExpediente()));
		assertThat(situacao.getAssunto(), is("7156 - MEDICAMENTOS E INSUMOS FARMACÊUTICOS - (Alteração na AFE) de DISTRIBUIDORA do produto - RESPONSÁVEL LEGAL"));
		assertThat(situacao.getProtocolo(), is("25352.259088/2012-66"));
		assertThat(situacao.getSituacao(), is("Em tramitação"));
//		assertThat(situacao.getEncontraSeNa(), is("COAFE"));
//		assertThat(situacao.getEncontraSeDesde(), is(new SimpleDateFormat("dd/MM/yyyy").parse("23/04/2012")));
//		assertThat(situacao.getDataPublicacao(), is(nullValue()));
			
		
	}

	public String recuperarArquivo(String arquivo){
		BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(arquivo)));
		String linha = null;
		StringBuffer sb = new StringBuffer();
		try {
			while((linha = reader.readLine())!= null){
				sb.append(linha).append("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}	
}
