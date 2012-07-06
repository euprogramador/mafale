package br.com.aexo.mafale.anvisa;

import org.junit.Test;

public class AnvisaTest {


	@Test
	public void deveriaFazerAConsultaAAnvisaRetornandoOsResultados() throws Exception {
		Servico servico = new Servico();
		
		servico.setProcesso("25351.297487/2005-05");
		servico.setExpediente("239266/11-6");
		servico.setCnpj("07075590000160"); 
		  
		Anvisa anvisa = new Anvisa();
		anvisa.consultar(servico); 
	}

}
