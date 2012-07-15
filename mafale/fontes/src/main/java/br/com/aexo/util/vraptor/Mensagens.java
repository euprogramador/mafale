package br.com.aexo.util.vraptor;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.ioc.Component;

/**
 * classe responsavel por exibir uma mensagem para o usuário
 * 
 * @author carlosr
 * 
 */
@Component
public class Mensagens {

	private Result result;

	/**
	 * monta as mensagens para poder exibir para o usuário
	 * 
	 * @param result
	 */
	public Mensagens(Result result) {
		this.result = result;
	}

	/**
	 * informa uma mensagem de sucesso
	 * 
	 * @param mensagem
	 */
	public void ok(String mensagem) {
		result.include("mensagem", new Mensagem("OK", mensagem));
	}

	/**
	 * informa uma mensagem de erro
	 * 
	 * @param mensagem
	 */
	public void erro(String mensagem) {
		result.include("mensagem", new Mensagem("ERRO", mensagem));
	}

}
