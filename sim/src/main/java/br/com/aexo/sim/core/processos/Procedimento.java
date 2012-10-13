package br.com.aexo.sim.core.processos;

/**
 * classe que executa diversos passos juntos
 * 
 * @author carlosr
 * 
 */

public class Procedimento implements Passo {

	private final Passo[] passos;

	public Procedimento(Passo... passos) {
		this.passos = passos;
	}

	@Override
	public void executar(Contexto contexto) {
		for (Passo passo : passos) {
			passo.executar(contexto);
		}
	}

}
