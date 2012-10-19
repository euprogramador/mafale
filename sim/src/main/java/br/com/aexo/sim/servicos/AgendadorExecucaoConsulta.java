package br.com.aexo.sim.servicos;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Stateless
public class AgendadorExecucaoConsulta {

	@Resource
	private TimerService timerService;
	
	@Inject
	private Event<Consulta> processadorConsulta;
	
	public void agendar(Consulta consulta){
		TimerConfig config = new TimerConfig();
		config.setInfo(consulta);
		config.setPersistent(false);
		timerService.createSingleActionTimer(consulta.getDelay(), config);
	}
	
	@Timeout
	public void processar(Timer timer){
		Consulta consulta = (Consulta) timer.getInfo();
		processadorConsulta.fire(consulta);
	}
}
