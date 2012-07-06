package br.com.aexo.mafale.anvisa;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

import br.com.aexo.Passo;

public class ColetaResultados implements Passo {

	@Override
	public void executar(Contexto contexto) {
		String consulta = contexto.get("consulta");
		Servico servico = contexto.get("servico");

		String exprInicio = "<tr>\r\n\t\t\t<td align=\"center\"><label>" + servico.getExpediente();
		int inicio = consulta.indexOf(exprInicio);
		String exprFim = "</tr>";
		int fim = consulta.indexOf(exprFim, inicio);
		String resultadoEncontrado = consulta.substring(inicio, fim + exprFim.length());
		resultadoEncontrado = resultadoEncontrado.replace("<label>", "").replace("<br>", "").replace("\t", "").replace("&nbsp;", "").replace("<b>", "|").replace("</b>", "|");
		System.out.println(resultadoEncontrado);
		final SituacaoNaAnvisa situacao = new SituacaoNaAnvisa();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser;
			saxParser = factory.newSAXParser();

			DefaultHandler handler = new AnvisaSaxHandler(situacao);
			saxParser.parse(new ByteArrayInputStream(resultadoEncontrado.getBytes()), handler);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		contexto.set("situacaoNaAnvisa", situacao);
	}
}
