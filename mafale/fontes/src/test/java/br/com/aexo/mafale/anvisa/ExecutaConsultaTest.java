package br.com.aexo.mafale.anvisa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.aexo.Contexto;

public class ExecutaConsultaTest {

	ExecutaConsulta consultador;
	private Contexto contexto;
	
	@Mock
	private HttpClient connection;
	
	@Mock
	private HttpPost request;
	
	@Mock
	private ClientConnectionManager connectionManager;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		contexto = new Contexto();
		consultador = new ExecutaConsulta();

		contexto.set("connection", connection);
		contexto.set("request", request);
		
		when(connection.getConnectionManager()).thenReturn(connectionManager);
	}

	@Test 
	public void deveriaExecutarAConsultaPassandoOsParametros() throws Exception {
		HttpResponse response = mock(HttpResponse.class);
		when(connection.execute(request)).thenReturn(response);

		HttpEntity entity = mock(HttpEntity.class);
		when(response.getEntity()).thenReturn(entity);
		String resultado = "testado";
		when(entity.getContent()).thenReturn(new ByteArrayInputStream(resultado.getBytes()));

		consultador.executar(contexto);
		
		assertThat(contexto.<String>get("consulta"), is(resultado));
		verify(connectionManager).shutdown();
	}

}
