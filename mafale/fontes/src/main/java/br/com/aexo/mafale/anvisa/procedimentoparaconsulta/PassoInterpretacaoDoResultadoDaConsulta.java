package br.com.aexo.mafale.anvisa.procedimentoparaconsulta;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import br.com.aexo.Contexto;
import br.com.aexo.Passo;
import br.com.aexo.mafale.DominioException;
import br.com.aexo.mafale.anvisa.PeticaoNaAnvisa;
import br.com.aexo.mafale.anvisa.ProcessoNaAnvisa;
import br.com.aexo.mafale.anvisa.procedimentoparaconsulta.sax.ProcessadorPeticaoRaiz;
import br.com.aexo.mafale.anvisa.procedimentoparaconsulta.sax.ProcessadorPeticoes;

public class PassoInterpretacaoDoResultadoDaConsulta implements Passo {

	@Override
	public void executar(Contexto contexto) {
		try {
			String resultadoDaConsulta = contexto.get("consulta");
			ProcessoNaAnvisa processo = contexto.get("processo");

			PeticaoNaAnvisa peticaoRaiz = interpretaPeticaoRaizDo(resultadoDaConsulta);
			processo.registra(peticaoRaiz);

			List<PeticaoNaAnvisa> peticoes = processaOutrasPeticoesNoArquivo(resultadoDaConsulta);
			for (PeticaoNaAnvisa peticao : peticoes) {
				processo.registra(peticao);
			}
		} catch (Exception e) {

			throw new DominioException("Não foi possivel executar a consulta");
		}
	}

	private PeticaoNaAnvisa interpretaPeticaoRaizDo(String resultado) throws ParserConfigurationException, SAXException, IOException {
		String inicioRaiz = "<table class=\"formulario\"";
		String fimRaiz = "</table>";
		int posInicioRaiz = resultado.indexOf(inicioRaiz);
		int posFimRaiz = resultado.indexOf(fimRaiz, posInicioRaiz);

		// remove caracteres e normaliza xml
		String consulta = resultado.substring(posInicioRaiz, posFimRaiz + fimRaiz.length());
		consulta = consulta.replace("<br>", "").replace("&nbsp;", "").replace("<b>", "|").replace("</b>", "|");
		consulta = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + consulta;

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		ProcessadorPeticaoRaiz handler = new ProcessadorPeticaoRaiz();
		saxParser.parse(new ByteArrayInputStream(consulta.getBytes()), handler);
		return handler.getPeticao();
	}

	private List<PeticaoNaAnvisa> processaOutrasPeticoesNoArquivo(String resultadoDaConsulta) throws ParserConfigurationException, SAXException, IOException {
		if (!resultadoDaConsulta.contains("PETIÇÕES</th>"))
			return new ArrayList<PeticaoNaAnvisa>();

		// posição da tabela raiz
		String inicioRaiz = "<table class=\"formulario\"";
		String fimRaiz = "</table>";
		int posInicioRaiz = resultadoDaConsulta.indexOf(inicioRaiz);
		int posFimRaiz = resultadoDaConsulta.indexOf(fimRaiz, posInicioRaiz);

		// posicao da tabela petições
		String inicioPeticao = "<table class=\"formulario\"";
		String fimPeticao = "</table>";
		int posInicioPeticao = resultadoDaConsulta.indexOf(inicioPeticao, posFimRaiz);
		int posFimPeticao = resultadoDaConsulta.indexOf(fimPeticao, posInicioPeticao);

		// remove caracteres e normaliza xml
		String consulta = resultadoDaConsulta.substring(posInicioPeticao, posFimPeticao + fimPeticao.length());
		consulta = consulta.replace("<br>", "").replace("&nbsp;", "").replace("<b>", "|").replace("</b>", "|").replace("height=20pt", "").replace("<label>", "").replace("nowrap", "")
				.replace("class=\"botaoBusca\">", "class=\"botaoBusca\"/>");
		consulta = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + consulta;

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		ProcessadorPeticoes handler = new ProcessadorPeticoes();
		saxParser.parse(new ByteArrayInputStream(consulta.getBytes()), handler);
		return handler.getPeticoes();
	}

}
