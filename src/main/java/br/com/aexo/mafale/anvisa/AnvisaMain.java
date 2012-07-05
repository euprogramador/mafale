package br.com.aexo.mafale.anvisa;

public class AnvisaMain {

	public static void main(String[] args) {
		Servico servico = new Servico();

		System.out.println("processo expediente cnpj");

		servico.setProcesso(args[0]);
		servico.setExpediente(args[1]);
		servico.setCnpj(args[2]);

		Anvisa anvisa = new Anvisa();
		anvisa.consultar(servico);
	}

}
