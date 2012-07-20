package br.com.aexo.util.vraptor.defaultcustomvalueresource;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import br.com.aexo.util.ajax.Data;
import br.com.aexo.util.dominio.Entidade;
import br.com.aexo.util.exceptions.DominioException;
import br.com.aexo.util.hibernate.MontadorAliasNaCriteria;
import br.com.aexo.util.vraptor.view.Json;
import br.com.aexo.util.vraptor.view.Status;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class CrudResource<E extends Entidade> {

	private final Result result;
	private final Validator validator;
	private final Session session;
	private final Class<? extends Entidade> classe;

	public CrudResource(Session session, Result result, Validator validator, Class<? extends Entidade> classe) {
		this.session = session;
		this.result = result;
		this.validator = validator;
		this.classe = classe;
	}

	@SuppressWarnings("unchecked")
	public void remover(E entidade) {
		entidade = (E) entidade.carregar();
		if (entidade == null) {
			result.notFound();
			return;
		}

		try {
			entidade.remover();
			result.use(Results.status()).ok();
		} catch (DominioException e) {
			result.use(Status.class).badRequest(e.getMessage());
		}
	}

	public void salvar(E entidade) {
		try {
			validator.validate(entidade);
			validator.onErrorSendBadRequest();
			if (validator.hasErrors())
				return;

			if (entidade.getId() == null) {
				entidade.salvar();
				result.use(Results.json()).withoutRoot().from(entidade).serialize();
				return;
			}

			Entidade db = entidade.carregar();
			db.preencherCom(entidade);
			db.salvar();
			result.use(Results.json()).withoutRoot().from(db).serialize();
		} catch (DominioException e) {
			result.use(Status.class).badRequest(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void listar(Order order, Integer inicio, Integer numRegistros, String filtro) {
		Data<E> data = new Data<E>();

		Criteria criteria = session.createCriteria(classe);
		criteria.addOrder(order);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		montar(filtro, criteria);

		if (inicio != null && inicio != 0)
			criteria.setFirstResult(inicio);

		if (numRegistros != null && numRegistros != 0)
			criteria.setMaxResults(numRegistros);

		data.setListagem(criteria.list());

		criteria = session.createCriteria(classe);
		criteria.setProjection(Projections.countDistinct("id"));
		montar(filtro, criteria);
		data.setContagem((Long) criteria.uniqueResult());

		data.setInicio(inicio);
		data.setNumRegistros(numRegistros);

		result.use(Json.class).withoutRoot().from(data).include("listagem").serialize();
	}

	private void montar(String filtro, Criteria criteria) {
		if (filtro == null)
			return;
		

		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.alias("filtro", Filtro.class);
		filtro = filtro.replace("{", "{\"filtro\":{").replace("},", "}},").replace("}]", "}}]");
		String filtroPassado = "{\"list\":" + filtro + "}";
		List<List<Filtro>> o = (List<List<Filtro>>) xstream.fromXML(filtroPassado);
		for (Filtro valorFiltro : o.get(0)) {
			processarFiltros(criteria, valorFiltro);
		}
	}

	private void montarAlias(String filtro,Criteria criteria) {
		new MontadorAliasNaCriteria().montar(classe, filtro, criteria);
	}

	
	private void processarFiltros(Criteria criteria, Filtro filtro) {
		String[] alvo = filtro.alvo.split("\\|");
		filtro.alvo = alvo[0];
		filtro.operando = alvo[1];
		filtro.tipo = alvo[2];
		
		if (filtro.valor != null &&  !filtro.valor.trim().isEmpty())
			montarAlias(filtro.alvo,criteria);
		
		filtro.alvo = filtro.alvo.replace(".", "_");
		
		char[] target = filtro.alvo.toCharArray();
		
		if (filtro.alvo.lastIndexOf("_")!=-1)
			target[filtro.alvo.lastIndexOf("_")] = '.';
		
		filtro.alvo = String.valueOf(target);
		
		
		
		Criterion criterion = null;
		if (filtro.valor == null)
			return;
		if (filtro.valor.trim().isEmpty())
			return;

		if (filtro.operando.equals("like"))
			criterion = Restrictions.ilike(filtro.alvo, filtro.valor, MatchMode.ANYWHERE);

		if (filtro.operando.equals("eq"))
			criterion = Restrictions.eq(filtro.alvo, convertePara(filtro.tipo, filtro.valor));
		if (filtro.operando.equals("lt"))
			criterion = Restrictions.lt(filtro.alvo, convertePara(filtro.tipo, filtro.valor));
		if (filtro.operando.equals("gt"))
			criterion = Restrictions.gt(filtro.alvo, convertePara(filtro.tipo, filtro.valor));
		if (filtro.operando.equals("le"))
			criterion = Restrictions.le(filtro.alvo, convertePara(filtro.tipo, filtro.valor));
		if (filtro.operando.equals("le"))
			criterion = Restrictions.le(filtro.alvo, convertePara(filtro.tipo, filtro.valor));

		
		
		criteria.add(criterion);
	}

	private Object convertePara(String tipo, String valor) {

		if (tipo.equals("string"))
			return valor;

		if (tipo.equals("long"))
			return new Long(valor);

		if (tipo.equals("integer"))
			return new Integer(valor);

		if (tipo.equals("datetime"))
			return new DateTime(valor);

		if (tipo.equals("localdate"))
			return new LocalDate(valor);

		if (tipo.equals("localtime"))
			return new LocalTime(valor);

		if (tipo.equals("localdatetime"))
			return new LocalDateTime(valor);

		if (tipo.equals("bigdecimal"))
			return new BigDecimal(valor);

		return valor;
	}

	@SuppressWarnings("unchecked")
	public void recuperar(Long id) {
		E entidade = (E) session.get(classe, id);
		if (entidade == null) {
			result.notFound();
			return;
		}
		result.use(Json.class).withoutRoot().from(entidade).serialize();
	}

}

class Filtro {
	String alvo;
	String operando;
	String valor;
	String tipo;
}