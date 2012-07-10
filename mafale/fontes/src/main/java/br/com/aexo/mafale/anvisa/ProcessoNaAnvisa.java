package br.com.aexo.mafale.anvisa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessoNaAnvisa {

	private String processo;
	private String cnpj;

	private List<PeticaoNaAnvisa> peticoes = new ArrayList<PeticaoNaAnvisa>();

	public String getProcesso() {
		return processo;
	}

	public void setProcesso(String processo) {
		this.processo = processo;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public void registra(PeticaoNaAnvisa peticao) {
		peticoes.add(peticao);
	}

	public List<PeticaoNaAnvisa> getPeticoes() {
		return Collections.unmodifiableList(peticoes);
	}

}
