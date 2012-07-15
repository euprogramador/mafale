package br.com.aexo.mafale.anvisa;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.aexo.mafale.anvisa.Anvisa;
import br.com.aexo.mafale.anvisa.ProcessoNaAnvisa;
import br.com.aexo.mafale.anvisa.procedimentoparaconsulta.ProcedimentoParaConsultaNaAnvisa;
import br.com.aexo.util.procedimentos.Contexto;

public class AnvisaTest {

	private Anvisa anvisa;
	@Mock
	private ProcedimentoParaConsultaNaAnvisa procedimentoParaConsultaNaAnvisa;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		anvisa = new Anvisa(procedimentoParaConsultaNaAnvisa);
	}

	@Test
	public void deveriaExecutarProcessoDeConsultaNaAnvisa() {
		ProcessoNaAnvisa processo = new ProcessoNaAnvisa();
		processo.setCnpj("321321321");
		processo.setProcesso("a13ad13213d21d");
		anvisa.consultar(processo);

		verify(procedimentoParaConsultaNaAnvisa).executar(Mockito.any(Contexto.class));
	}

}
