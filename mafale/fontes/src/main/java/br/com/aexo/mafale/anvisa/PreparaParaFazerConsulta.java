package br.com.aexo.mafale.anvisa;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

import br.com.aexo.Passo;
import br.com.aexo.mafale.DominioException;

public class PreparaParaFazerConsulta implements Passo {

	protected Properties config = new Properties();

	@Override
	public void executar(Contexto contexto) {
		InputStream stream = getClass().getClassLoader().getResourceAsStream("anvisa.properties");
		carregarConfiguracoes(stream);
		DefaultHttpClient connection = new DefaultHttpClient();

		HttpPost request = new HttpPost("http://www7.anvisa.gov.br/datavisa/Consulta_Processos/Resultado_Processos_Detalhe.asp");
		if (config.get("proxy").equals("true")) {
			HttpHost proxy = new HttpHost(config.getProperty("proxy.host"), Integer.parseInt(config.getProperty("proxy.port")));
			connection.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

			if (config.get("proxy.autenticado").equals("true")) {
				String basic_auth = new String(Base64.encodeBase64((config.getProperty("proxy.usuario") + ":" + config.getProperty("proxy.senha")).getBytes()));
				request.addHeader("proxy-authorization", "Basic " + basic_auth);
			}
		}

		contexto.set("connection", connection);
		contexto.set("request", request);
	}

	protected void carregarConfiguracoes(InputStream stream) {
		try {
			config.load(stream);
		} catch (Exception e) {
			throw new DominioException(e);
		}

		if (!config.containsKey("proxy"))
			throw new DominioException("configuração para site da anvisa inconsistente chave \"proxy\" não configurada");
		if (!config.containsKey("proxy.host"))
			throw new DominioException("configuração para site da anvisa inconsistente chave \"proxy.host\" não configurada");

		if (!config.containsKey("proxy.port"))
			throw new DominioException("configuração para site da anvisa inconsistente chave \"proxy.port\" não configurada");

		if (!config.containsKey("proxy.autenticado"))
			throw new DominioException("configuração para site da anvisa inconsistente chave \"proxy.autenticado\" não configurada");

		if (!config.containsKey("proxy.usuario"))
			throw new DominioException("configuração para site da anvisa inconsistente chave \"proxy.usuario\" não configurada");

		if (!config.containsKey("proxy.senha"))
			throw new DominioException("configuração para site da anvisa inconsistente chave \"proxy.senha\" não configurada");

	}

}
