package br.com.aexo.util.hibernate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.Criteria;

public class MontadorAliasNaCriteria {

	public void montar(Class<?> root, String filtro, Criteria criteria) {
		montar(root, "", filtro, criteria);
	}

	private void montar(Class<?> root, String pai, String filtro, Criteria criteria) {
		List<String> aliases = new ArrayList<String>(Arrays.asList(filtro.split("\\.")));
		final String target = aliases.get(0);
		aliases.remove(0);

		Field field = new Mirror().on(root).reflect().field(target);

		if (field == null)
			throw new RuntimeException(pai+"."+filtro+" não existe como relação");

		
		if (!eUmaRelacaoO(field)) {
			return;
		}

		Class<?> classTarget = recuperaOTipo(field);

		String alias = "";
		String associacao = "";
		if (pai.equals("")) {
			alias = target;
			associacao =  field.getName();
		}else {
			alias = pai + "_" + target;
			associacao =  pai+"."+field.getName();
		} 

		criteria.createAlias(associacao,alias);
		montar(classTarget, target, montarSubAlias(aliases), criteria);

	}

	private Class<?> recuperaOTipo(Field field) {
		Class<?> classTarget = field.getType();

		if (Collection.class.isAssignableFrom(classTarget)) {
			ParameterizedType pt = (ParameterizedType) field.getGenericType();
			classTarget = (Class<?>) pt.getActualTypeArguments()[0];
		}
		return classTarget;
	}

	private boolean eUmaRelacaoO(Field field) {
		return field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToOne.class);
	}

	private String montarSubAlias(List<String> aliases) {
		StringBuffer sb = new StringBuffer();
		for (String alias : aliases) {
			sb.append(alias);
			sb.append(".");
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		return sb.toString();
	}
	
}


