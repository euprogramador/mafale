package br.com.aexo.mafale.anvisa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ProcessadorPeticoes extends DefaultHandler {

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

			if (peticao.getExpediente()!=null)
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
		
		try {
			String[] publicacao = buffer.toString().trim().replace(")", "").split("\\(");
			getPeticao().setResolucao(publicacao[1]);
			getPeticao().setDataPublicacao(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse(publicacao[0])));
		} catch (Exception e) {
			// excepcted
		}
	}

	private void extraiLocalizacao() {
		String[] localizacao = buffer.toString().trim().split("\\|");
		getPeticao().setEncontraSeNa((localizacao[1].contains("Desde") ? "Encontra-se na " : "Encaminhado para ") + localizacao[0]);

		try {
			getPeticao().setEncontraSeDesde(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse(localizacao[2])));
		} catch (Exception e) {
			// excepcted
		}
	}

	private void extraiSituacao() {
		getPeticao().setSituacao(buffer.toString().trim());
	}

	private void extraiAssunto() {
		getPeticao().setAssunto(buffer.toString().trim());
	}

	private void extraiProtocolo() {
		getPeticao().setProtocolo(buffer.toString().trim());
	}

	private void extraiExpediente() {
		String[] expediente = buffer.toString().trim().replace(")", "").split("\\(");
		getPeticao().setExpediente(expediente[0]);
		try {
			getPeticao().setDataEntrada(new LocalDate(new SimpleDateFormat("dd/MM/yyyy").parse(expediente[1])));
		} catch (Exception e) {
			// expected
		}
	}

	public PeticaoNaAnvisa getPeticao() {
		return peticao;
	}

	public List<PeticaoNaAnvisa> getPeticoes() {
		return peticoes;
	}

}