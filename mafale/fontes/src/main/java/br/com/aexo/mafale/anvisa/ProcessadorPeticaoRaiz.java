package br.com.aexo.mafale.anvisa;

import java.text.SimpleDateFormat;

import org.joda.time.LocalDate;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * classe responsavel por fazer o processamento de um elemento raiz de uma
 * peticao
 * 
 * @author carlosr
 * 
 */
public class ProcessadorPeticaoRaiz extends DefaultHandler {

	private final PeticaoNaAnvisa peticao = new PeticaoNaAnvisa();

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
		}

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
				extraiDataEntrada();
				break;
			case 5:
				extraiSituacao();
				break;
			case 6:
				extraiLocalizacao();
				break;
			case 7:
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
		getPeticao().setResolucao(publicacao[1]);
		try {
			getPeticao().setDataPublicacao(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse(publicacao[0])));
		} catch (Exception e) {
		}
	}

	private void extraiLocalizacao() {
		String[] localizacao = buffer.toString().trim().split("\\|");
		getPeticao().setEncontraSeNa((localizacao[1].contains("Desde") ? "Encontra-se na " : "Encaminhado para ") + localizacao[0]);

		try {
			getPeticao().setEncontraSeDesde(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse(localizacao[2])));
		} catch (Exception e) {
		}
	}

	private void extraiSituacao() {
		getPeticao().setSituacao(buffer.toString().trim());
	}

	private void extraiDataEntrada() {
		try {
			getPeticao().setDataEntrada(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse(buffer.toString().trim())));
		} catch (Exception e) {
		}
	}

	private void extraiAssunto() {
		getPeticao().setAssunto(buffer.toString().trim());
	}

	private void extraiProtocolo() {
		getPeticao().setProtocolo(buffer.toString().trim());
	}

	private void extraiExpediente() {
		getPeticao().setExpediente(buffer.toString().trim().replace(")", "").split("\\(")[1]);
	}

	public PeticaoNaAnvisa getPeticao() {
		return peticao;
	}

}