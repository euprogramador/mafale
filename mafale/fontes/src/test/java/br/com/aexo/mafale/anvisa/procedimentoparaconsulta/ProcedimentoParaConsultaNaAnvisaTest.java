package br.com.aexo.mafale.anvisa.procedimentoparaconsulta;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

import br.com.aexo.mafale.anvisa.procedimentoparaconsulta.PassoExecucaoDaConsulta;
import br.com.aexo.mafale.anvisa.procedimentoparaconsulta.PassoInterpretacaoDoResultadoDaConsulta;
import br.com.aexo.mafale.anvisa.procedimentoparaconsulta.ProcedimentoParaConsultaNaAnvisa;
import br.com.aexo.util.procedimentos.Contexto;

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
