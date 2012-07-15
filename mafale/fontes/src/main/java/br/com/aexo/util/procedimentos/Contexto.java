package br.com.aexo.util.procedimentos;

import java.util.HashMap;
import java.util.Map;

public class Contexto {

	private Map<String, Object> contexto = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public <T> T get(String chave) {
		return (T) contexto.get(chave);
	}

	public void set(String chave, Object valor) {
		contexto.put(chave, valor);
	}
}
