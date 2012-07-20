package br.com.aexo.util.vraptor.defaultcustomvalueresource;

import java.io.File;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import br.com.aexo.util.compiler.Compiler;
import br.com.aexo.util.compiler.SourceCode;
import br.com.aexo.util.dominio.Entidade;

/**
 * gera a classe de crud de forma dinamica
 * 
 * @author carlosr
 * 
 */

public abstract class CrudService {

	private Map<Class<? extends Entidade>, Class<? extends CrudResource<?>>> cruds = new HashMap<Class<? extends Entidade>, Class<? extends CrudResource<?>>>();

	private Map<Class<? extends CrudResource<?>>, Class<? extends CrudResource<?>>> crudMap = new HashMap<Class<? extends CrudResource<?>>, Class<? extends CrudResource<?>>>();

	private final ServletContext servletContext;

	public CrudService(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	private void makeCrudServices() {
		List<Crud> cruds = getCruds();

		for (Crud crud : cruds) {
			Class<? extends CrudResource<?>> customCrud = crud(crud.getEntidade());
			this.cruds.put(crud.getEntidade(), customCrud);
			crudMap.put(customCrud, customCrud);
		}
	}

	public abstract List<Crud> getCruds();

	public boolean contains(Class<?> type) {
		return crudMap.containsKey(type);
	}

	public Class<? extends CrudResource<?>> getCrudFor(Class<? extends Entidade> entity) {
		synchronized (cruds) {
			if (cruds.isEmpty())
				makeCrudServices();
		}

		return cruds.get(entity);
	}

	private <T extends Entidade> Class<? extends CrudResource<T>> crud(Class<T> ent) {

		StringBuffer sb = new StringBuffer();
		sb.append("import br.com.caelum.vraptor.*;\r\n");
		sb.append("import org.hibernate.Session;\r\n");
		sb.append("import org.hibernate.criterion.*;\r\n");
		sb.append("@br.com.caelum.vraptor.Resource\r\n");
		sb.append("public class CrudResourceFor" + ent.getSimpleName() + " extends " + CrudResource.class.getName() + "<" + ent.getName() + ">{\r\n");
		sb.append("");
		sb.append("public CrudResourceFor" + ent.getSimpleName() + "(Session session, Result result, Validator validator) {\r\n");
		sb.append("super(session, result, validator," + ent.getName() + ".class);\r\n");
		sb.append("}\r\n");
		sb.append("\r\n");
		sb.append("public void remover(@javax.inject.Named(\"entidade\")" + ent.getName() + " entidade) {\r\n");
		sb.append("super.remover(entidade);\r\n");
		sb.append("}\r\n");
		sb.append("\r\n");
		sb.append("@br.com.caelum.vraptor.Consumes\r\n");
		sb.append("public void salvar(@javax.inject.Named(\"entidade\")" + ent.getName() + " entidade) {\r\n");
		sb.append("super.salvar(entidade);\r\n");
		sb.append("}\r\n");
		sb.append("\r\n");
		sb.append("public void listar(@javax.inject.Named(\"order\")Order order, @javax.inject.Named(\"inicio\")Integer inicio, @javax.inject.Named(\"numRegistros\")Integer numRegistros,@javax.inject.Named(\"filtro\")String filtro) {\r\n");
		sb.append("super.listar(order, inicio, numRegistros,filtro);\r\n");
		sb.append("}\r\n");
		sb.append("\r\n");
		sb.append("public void recuperar( @javax.inject.Named(\"id\")Long id) {\r\n");
		sb.append("super.recuperar(id);\r\n");
		sb.append("}\r\n");
		sb.append("\r\n");
		sb.append("}\r\n");

		Compiler compiler = new Compiler();

		SourceCode sourceCode = new SourceCode("CrudResourceFor" + ent.getSimpleName(), sb.toString());

		sb = new StringBuffer();
		sb.append(System.getProperty("java.class.path"));
		sb.append(File.pathSeparator);
		sb.append(new File(servletContext.getRealPath("/WEB-INF/classes")).getAbsolutePath());

		for (File file : new File(servletContext.getRealPath("/WEB-INF/lib")).listFiles()) {
			sb.append(File.pathSeparator).append(file.getAbsolutePath());
		}

		StringWriter out = new StringWriter();
		ClassLoader loader = compiler.compile(Arrays.asList(sourceCode), sb.toString(), out);

		System.out.println(out.toString());

		try {
			Thread.currentThread().setContextClassLoader(loader);
			@SuppressWarnings("unchecked")
			Class<? extends CrudResource<T>> loadClass = (Class<? extends CrudResource<T>>) loader.loadClass("CrudResourceFor" + ent.getSimpleName());
			return loadClass;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException();
		}
	}

}
