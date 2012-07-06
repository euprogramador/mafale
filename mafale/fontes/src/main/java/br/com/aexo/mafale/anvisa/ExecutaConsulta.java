package br.com.aexo.mafale.anvisa;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import br.com.aexo.Passo;
import br.com.aexo.mafale.DominioException;

public class ExecutaConsulta implements Passo {

	@Override
	public void executar(Contexto contexto) {
		HttpClient connection = contexto.get("connection");
		HttpPost request = contexto.get("request");
		Servico servico = contexto.get("servico");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("hdnProcesso", servico.getProcesso().replace(".", "").replace("/", "").replace("-", "")));
		params.add(new BasicNameValuePair("hdnCNPJ", servico.getCnpj().replace(".", "").replace("/", "").replace("-", "")));
		params.add(new BasicNameValuePair("opTipo", "TEC"));
		params.add(new BasicNameValuePair("NU_PROCESSO", servico.getProcesso()));
		try {
			try { 

				UrlEncodedFormEntity paramEntity = new UrlEncodedFormEntity(params);
				request.setEntity(paramEntity);
				request.addHeader("content-type", "application/x-www-form-urlencoded");

				HttpResponse response = connection.execute(request);
				HttpEntity entity = response.getEntity();
				if (response.getStatusLine().getStatusCode() != 200) {
					System.out.println(EntityUtils.toString(entity));
					throw new DominioException("Não foi possivel executar a consulta devido a um erro no servidor");
				}
				contexto.set("consulta", EntityUtils.toString(entity));
			} finally {
				connection.getConnectionManager().shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DominioException("Não foi possivel executar a consulta, verifique o log para maiores detalhes");
		}
	}

}
