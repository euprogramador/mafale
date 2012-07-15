package br.com.aexo.util.vraptor;

/**
 * representa uma mensagem que é exibida para o usuário
 * 
 * @author carlosr
 * 
 */
public class Mensagem {

	private final String tipo;
	private final String mensagem;

	/**
	 * cria uma mensagem
	 * 
	 * @param tipo
	 * @param mensagem
	 */
	public Mensagem(String tipo, String mensagem) {
		this.tipo = tipo;
		this.mensagem = mensagem;
	}

	/**
	 * qual o tipo da mensagem
	 * 
	 * @return
	 */
	// TODO não seria melhor usar uma enum
	public String getTipo() {
		return tipo;
	}

	/**
	 * qual a mensagem
	 * 
	 * @return
	 */
	public String getMensagem() {
		return mensagem;
	}

}