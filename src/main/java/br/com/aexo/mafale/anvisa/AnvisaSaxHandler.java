package br.com.aexo.mafale.anvisa;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * leitor sax do resultado da anvisa
 * 
 * @author carlosr
 * 
 */
public class AnvisaSaxHandler extends DefaultHandler {
	Integer posicao = 0;
	StringBuffer data = new StringBuffer();
	private final SituacaoNaAnvisa situacao;

	public AnvisaSaxHandler(SituacaoNaAnvisa situacao) {
		this.situacao = situacao;
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (posicao == 0) {
			String dado = data.toString().trim().replace("(", "%");
			situacao.setExpediente(dado.split("%")[0]);
			try {
				situacao.setDataEntrada(new SimpleDateFormat("dd/MM/yyyy").parse(dado.split("%")[1].replace(")", "")));
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		if (posicao == 1)
			situacao.setProtocolo(data.toString().trim());
		if (posicao == 2)
			situacao.setAssunto(data.toString().trim());
		if (posicao == 3)
			situacao.setSituacao(data.toString().trim());
		if (posicao == 4) {
			String dado = data.toString().trim();
			String localizacao = dado.split("\\|")[1].contains("Desde") ? "Encontra-se na" : "Encaminhado para"; 
			situacao.setEncontraSeNa(localizacao+" "+dado.split("\\|")[0]);
			try {
				situacao.setEncontraSeDesde(new SimpleDateFormat("dd/MM/yyyy").parse(dado.split("\\|")[2]));
			} catch (ParseException e) {
			}
		}
		if (posicao == 5) {
			try {
				String dado = data.toString().trim().replace("(", "%");
				situacao.setDataPublicacao(new SimpleDateFormat("dd/MM/yyyy").parse(dado.split("%")[0]));
				situacao.setResolucao(dado.split("%")[1].replace(")", ""));
			} catch (ParseException e) {
			}
		}

		posicao++;
		data = new StringBuffer();
	}

	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}

};
