package br.com.aexo.mafale.anvisa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.ByteArrayInputStream;

import junit.framework.Assert;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;

import br.com.aexo.Contexto;

public class PreparaParaFazerConsultaTest {

	private PreparaParaFazerConsulta consulta;

	@Before
	public void setup() {
		consulta = new PreparaParaFazerConsulta();
	}

	@Test
	public void aoCarregarConfiguracoesDeveriaConferirSeChavesExistem() {
		try {
			consulta.carregarConfiguracoes(new ByteArrayInputStream("".getBytes()));
			Assert.fail("Deveria apontar que arquivo não foi configurado corretamente");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy\" não configurada"));
		}

		try {
			consulta.carregarConfiguracoes(new ByteArrayInputStream("proxy=false\r\n".getBytes()));
			Assert.fail("Deveria apontar que arquivo não foi configurado corretamente");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy.host\" não configurada"));
		}

		try {
			consulta.carregarConfiguracoes(new ByteArrayInputStream("proxy=false\r\nproxy.host=\r\n".getBytes()));
			Assert.fail("Deveria apontar que arquivo não foi configurado corretamente");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy.port\" não configurada"));
		}

		try {
			consulta.carregarConfiguracoes(new ByteArrayInputStream("proxy=false\r\nproxy.host=\r\nproxy.port=".getBytes()));
			Assert.fail("Deveria apontar que arquivo não foi configurado corretamente");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy.autenticado\" não configurada"));
		}

		try {
			consulta.carregarConfiguracoes(new ByteArrayInputStream("proxy=false\r\nproxy.host=\r\nproxy.port=\r\nproxy.autenticado".getBytes()));
			Assert.fail("Deveria apontar que arquivo não foi configurado corretamente");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy.usuario\" não configurada"));
		}

		try {
			consulta.carregarConfiguracoes(new ByteArrayInputStream("proxy=false\r\nproxy.host=\r\nproxy.port=\r\nproxy.autenticado\r\nproxy.usuario".getBytes()));
			Assert.fail("Deveria apontar que arquivo não foi configurado corretamente");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy.senha\" não configurada"));
		}
	}

	@Test
	public void deveriaPrepararConexaoComAnvisaParaFazerConsulta() {
		Contexto contexto = new Contexto();
		consulta.executar(contexto);
		assertThat(contexto.get("connection"), is(notNullValue()));
		assertThat(contexto.get("connection"), is(instanceOf(HttpClient.class)));

		assertThat(contexto.get("request"), is(notNullValue()));
		assertThat(contexto.get("request"), is(instanceOf(HttpPost.class)));

	}

}
