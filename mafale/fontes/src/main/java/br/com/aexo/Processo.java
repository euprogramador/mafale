package br.com.aexo;

import br.com.aexo.mafale.anvisa.Contexto;

public class Processo implements Passo {

	private final Passo[] passos;

	public Processo(Passo... passos) {
		this.passos = passos;
	}

	@Override
	public void executar(Contexto contexto) {
		for (Passo passo : passos) {
			passo.executar(contexto);
		}
	}

}
