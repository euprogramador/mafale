package br.com.aexo.mafale.novo.consultador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.ByteArrayInputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.com.aexo.mafale.anvisa.PassoExecucaoDaConsulta;

public class PassoExecucaoDaConsultaTest {

	private PassoExecucaoDaConsulta executor;

	@Before
	public void setup() {
		executor = new PassoExecucaoDaConsulta();
	}

	
	// TODO implementar Testes de unidade para a execução da consulta
	
	@Test
	public void deveriaCarregarConfiguracoesDeExecucao() {

		String parametros = "";
		try {
			executor.carregarConfiguracoes(new ByteArrayInputStream(parametros.getBytes()));
			Assert.fail("deveria ter informado erro no carregamento do parametro");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy\" não configurada"));
		}

		parametros = "proxy=teste";
		try {
			executor.carregarConfiguracoes(new ByteArrayInputStream(parametros.getBytes()));
			Assert.fail("deveria ter informado erro no carregamento do parametro");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy.host\" não configurada"));
		}

		parametros = "proxy=teste\rproxy.host=teste";
		try {
			executor.carregarConfiguracoes(new ByteArrayInputStream(parametros.getBytes()));
			Assert.fail("deveria ter informado erro no carregamento do parametro");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy.port\" não configurada"));
		}

		parametros = "proxy=teste\rproxy.host=teste\rproxy.port=1";
		try {
			executor.carregarConfiguracoes(new ByteArrayInputStream(parametros.getBytes()));
			Assert.fail("deveria ter informado erro no carregamento do parametro");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy.autenticado\" não configurada"));
		}

		parametros = "proxy=teste\rproxy.host=teste\rproxy.port=1\rproxy.autenticado";
		try {
			executor.carregarConfiguracoes(new ByteArrayInputStream(parametros.getBytes()));
			Assert.fail("deveria ter informado erro no carregamento do parametro");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy.usuario\" não configurada"));
		}

		parametros = "proxy=teste\rproxy.host=teste\rproxy.port=1\rproxy.autenticado=true\rproxy.usuario";
		try {
			executor.carregarConfiguracoes(new ByteArrayInputStream(parametros.getBytes()));
			Assert.fail("deveria ter informado erro no carregamento do parametro");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"proxy.senha\" não configurada"));
		}

		parametros = "proxy=teste\rproxy.host=teste\rproxy.port=1\rproxy.autenticado=true\rproxy.usuario=teste\rproxy.senha";
		try {
			executor.carregarConfiguracoes(new ByteArrayInputStream(parametros.getBytes()));
			Assert.fail("deveria ter informado erro no carregamento do parametro");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("configuração para site da anvisa inconsistente chave \"url.consulta\" não configurada"));
		}

	}

}
