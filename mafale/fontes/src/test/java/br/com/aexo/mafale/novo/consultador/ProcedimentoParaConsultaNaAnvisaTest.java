package br.com.aexo.mafale.novo.consultador;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

import br.com.aexo.Contexto;
import br.com.aexo.mafale.anvisa.PassoExecucaoDaConsulta;
import br.com.aexo.mafale.anvisa.PassoInterpretacaoDoResultadoDaConsulta;
import br.com.aexo.mafale.anvisa.ProcedimentoParaConsultaNaAnvisa;

public class ProcedimentoParaConsultaNaAnvisaTest {

	@Test
	public void deveriaInvocarOsPassosDoProcedimento() {
		PassoExecucaoDaConsulta passoExecucaoDaConsulta = mock(PassoExecucaoDaConsulta.class);
		PassoInterpretacaoDoResultadoDaConsulta passoInterpretacaoDoResultadoDaConsulta = mock(PassoInterpretacaoDoResultadoDaConsulta.class);

		ProcedimentoParaConsultaNaAnvisa procedimento = new ProcedimentoParaConsultaNaAnvisa(passoExecucaoDaConsulta, passoInterpretacaoDoResultadoDaConsulta);
		procedimento.executar(new Contexto());
		verify(passoExecucaoDaConsulta).executar(Mockito.any(Contexto.class));
		verify(passoInterpretacaoDoResultadoDaConsulta).executar(Mockito.any(Contexto.class));
	}

}
