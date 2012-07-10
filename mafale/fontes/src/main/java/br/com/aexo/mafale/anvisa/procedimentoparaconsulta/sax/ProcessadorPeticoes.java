package br.com.aexo.mafale.anvisa.procedimentoparaconsulta.sax;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.aexo.mafale.anvisa.PeticaoNaAnvisa;

public class ProcessadorPeticoes extends DefaultHandler {

	private static final Logger log = LoggerFactory.getLogger(ProcessadorPeticoes.class);

	private List<PeticaoNaAnvisa> peticoes = new ArrayList<PeticaoNaAnvisa>();
	private PeticaoNaAnvisa peticao = new PeticaoNaAnvisa();

	int contagem = 0;
	StringBuffer buffer = new StringBuffer();

	@Override
	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
		if (qName.equals("td")) {
			buffer = new StringBuffer();
			contagem++;
		}
		if (qName.equals("tr")) {
			contagem = 0;

			if (peticao.getExpediente() != null)
				peticoes.add(peticao);

			peticao = new PeticaoNaAnvisa();
		}

	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (qName.equals("td")) {
			switch (contagem) {
			case 1:
				extraiExpediente();
				break;
			case 2:
				extraiProtocolo();
				break;
			case 3:
				extraiAssunto();
				break;
			case 4:
				extraiSituacao();
				break;
			case 5:
				extraiLocalizacao();
				break;
			case 6:
				extraiPublicacaoEResolucao();
				break;
			}
		}

	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		buffer.append(new String(ch, start, length));
	}

	private void extraiPublicacaoEResolucao() {
		String[] publicacao = buffer.toString().trim().replace(")", "").split("\\(");

		try {
			getPeticao().setResolucao(publicacao[1]);
			getPeticao().setDataPublicacao(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse(publicacao[0])));
			log.debug("extraido a resolução {}", new Object[] { publicacao[1] });
			log.debug("extraido data de publicação {}", new Object[] { publicacao[0] });
		} catch (Exception e) {
			log.debug("data de publicação não extraida. conteúdo não é uma data {}", new Object[] { buffer.toString().trim() });
			log.trace("ocorreu um erro ao extrair a data de publicação este erro porém não afeta a continuidade da aplicação", e);
		}
	}

	private void extraiLocalizacao() {
		String[] localizacao = buffer.toString().trim().split("\\|");
		String encontraSeNa = (localizacao[1].contains("Desde") ? "Encontra-se na " : "Encaminhado para ") + localizacao[0];
		log.debug("extraido a localização {}", new Object[] { encontraSeNa });
		getPeticao().setEncontraSeNa(encontraSeNa);

		try {
			log.debug("extraido a data de localizacao {}", new Object[] { buffer.toString().trim() });
			getPeticao().setEncontraSeDesde(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse(localizacao[2])));
		} catch (Exception e) {
			log.debug("data de localização não extraida. conteúdo não é uma data {}", new Object[] { buffer.toString().trim() });
			log.trace("ocorreu um erro ao extrair a data de localização este erro porém não afeta a continuidade da aplicação", e);
		}
	}

	private void extraiSituacao() {
		log.debug("extraido a situação {}", new Object[] { buffer.toString().trim() });
		getPeticao().setSituacao(buffer.toString().trim());
	}

	private void extraiAssunto() {
		log.debug("extraido o assunto {}", new Object[] { buffer.toString().trim() });
		getPeticao().setAssunto(buffer.toString().trim());
	}

	private void extraiProtocolo() {
		log.debug("extraido o protocolo {}", new Object[] { buffer.toString().trim() });
		getPeticao().setProtocolo(buffer.toString().trim());
	}

	private void extraiExpediente() {
		String[] expediente = buffer.toString().trim().replace(")", "").split("\\(");
		try {
			log.debug("extraido o expediente {}", new Object[] { expediente[0] });
			getPeticao().setExpediente(expediente[0]);
			log.debug("extraido a data de entrada {}", new Object[] { expediente[1] });
			getPeticao().setDataEntrada(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse(expediente[1].trim())));
		} catch (Exception e) {
			log.debug("data de entrada não extraida. conteúdo não é uma data {}", new Object[] { buffer.toString().trim()});
			log.trace("ocorreu um erro ao extrair a data de entrada este erro porém não afeta a continuidade da aplicação", e);
		}
	}

	public PeticaoNaAnvisa getPeticao() {
		return peticao;
	}

	public List<PeticaoNaAnvisa> getPeticoes() {
		return peticoes;
	}

}