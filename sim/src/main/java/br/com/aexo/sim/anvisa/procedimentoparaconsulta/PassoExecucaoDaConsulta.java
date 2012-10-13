package br.com.aexo.sim.anvisa.procedimentoparaconsulta;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import br.com.aexo.sim.anvisa.ProcessoNaAnvisa;
import br.com.aexo.sim.core.processos.Contexto;
import br.com.aexo.sim.core.processos.Passo;
import br.com.aexo.sim.core.util.exceptions.ConsultaException;

public class PassoExecucaoDaConsulta implements Passo {
	protected Properties config = new Properties();
	private HttpPost request;

	@Override
	public void executar(Contexto contexto) {
		
		ProcessoNaAnvisa processo = contexto.get("processo");
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("anvisa.properties");
		carregarConfiguracoes(stream);
		DefaultHttpClient connection = new DefaultHttpClient();

		request = new HttpPost(config.getProperty("url.consulta"));
		if (config.get("proxy").equals("true")) {
			HttpHost proxy = new HttpHost(config.getProperty("proxy.host"), Integer.parseInt(config.getProperty("proxy.port")));
			connection.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

			if (config.get("proxy.autenticado").equals("true")) {
				String basic_auth = new String(Base64.encodeBase64((config.getProperty("proxy.usuario") + ":" + config.getProperty("proxy.senha")).getBytes()));
				request.addHeader("proxy-authorization", "Basic " + basic_auth);
			}
		}
		

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("hdnProcesso", processo.getProcesso().replace(".", "").replace("/", "").replace("-", "")));
		params.add(new BasicNameValuePair("hdnCNPJ", processo.getCnpj().replace(".", "").replace("/", "").replace("-", "")));
		params.add(new BasicNameValuePair("opTipo", "TEC"));
		params.add(new BasicNameValuePair("NU_PROCESSO", processo.getProcesso()));
		try {
			try {

				UrlEncodedFormEntity paramEntity = new UrlEncodedFormEntity(params);
				request.setEntity(paramEntity);
				request.addHeader("content-type", "application/x-www-form-urlencoded");

				HttpResponse response = connection.execute(request);
				HttpEntity entity = response.getEntity();
				if (response.getStatusLine().getStatusCode() != 200) {
					System.out.println(EntityUtils.toString(entity));
					throw new ConsultaException("Não foi possivel executar a consulta devido a um erro no servidor");
				}
				contexto.set("consulta", EntityUtils.toString(entity));
			} finally {
				connection.getConnectionManager().shutdown();
			}
		} catch (Exception e) {
			throw new ConsultaException("Não foi possivel executar a consulta, verifique o log para maiores detalhes");
		}

	}

	protected void carregarConfiguracoes(InputStream stream) {
		try {
			config.load(stream);
		} catch (Exception e) {
			throw new ConsultaException(e);
		}

		if (!config.containsKey("proxy"))
			throw new ConsultaException("configuração para site da anvisa inconsistente chave \"proxy\" não configurada");
		if (!config.containsKey("proxy.host"))
			throw new ConsultaException("configuração para site da anvisa inconsistente chave \"proxy.host\" não configurada");

		if (!config.containsKey("proxy.port"))
			throw new ConsultaException("configuração para site da anvisa inconsistente chave \"proxy.port\" não configurada");

		if (!config.containsKey("proxy.autenticado"))
			throw new ConsultaException("configuração para site da anvisa inconsistente chave \"proxy.autenticado\" não configurada");

		if (!config.containsKey("proxy.usuario"))
			throw new ConsultaException("configuração para site da anvisa inconsistente chave \"proxy.usuario\" não configurada");

		if (!config.containsKey("proxy.senha"))
			throw new ConsultaException("configuração para site da anvisa inconsistente chave \"proxy.senha\" não configurada");
		
		if (!config.containsKey("url.consulta"))
			throw new ConsultaException("configuração para site da anvisa inconsistente chave \"url.consulta\" não configurada");

	}
}
