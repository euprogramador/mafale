package br.com.aexo.mafale.administrativo;

import javax.persistence.Entity;

import org.joda.time.LocalDate;

@Entity
public class Servico {

	private Long id;
	private Cliente cliente;
	private Assunto assunto;
	private TipoServico tipoServico;
	private String produto;
	private String transacao;
	private LocalDate entrada;
	private String protocoloManual;
	private String expediente;
	private String processo;

}
