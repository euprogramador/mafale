package br.com.aexo.util.vraptor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.servlet.ServletContext;

import org.scannotation.AnnotationDB;

import br.com.aexo.util.exceptions.InfraestruturaException;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

/**
 * Procura por entidades no classpath
 * 
 * @author carlosr
 * 
 */
@Component
@ApplicationScoped
class EntityScanner {

	private final ServletContext context;

	/**
	 * cria uma entity scanner
	 * 
	 * @param context
	 *            - contexto para saber o realpath do lib e classes da aplicação
	 *            web
	 */
	public EntityScanner(ServletContext context) {
		this.context = context;
	}

	/**
	 * scanneia o classpath a procura de entidades
	 * 
	 * @return
	 */
	public List<Class<?>> scan() {
		AnnotationDB db = retriveConfiguredAnnotationDB();

		Map<String, Set<String>> scanned = scanClassesIn(db);

		Set<String> entityClassesName = scanned.get(Entity.class.getCanonicalName());

		return obtainClassesFromList(entityClassesName);
	}

	/**
	 * obtem uma lista das classes que são entidades
	 * 
	 * @param entityClassesName
	 *            nomes das classes
	 * @return returna a classe
	 */
	private List<Class<?>> obtainClassesFromList(Set<String> entityClassesName) {

		List<Class<?>> entitys = new ArrayList<Class<?>>();
		if (entityClassesName != null) {
			for (String classe : entityClassesName) {
				try {
					entitys.add(Class.forName(classe));
				} catch (Exception e) {
					throw new InfraestruturaException("Não foi possivel obter as entidades do classpath", e);
				}
			}
		}
		return entitys;
	}

	/**
	 * procura por entidades nas bibliotecas da aplicação WEB-INF/lib
	 * 
	 * @param db
	 * @return
	 */

	private Map<String, Set<String>> scanClassesIn(AnnotationDB db) {
		try {
			db.scanArchives(retrivePathOnClasses());
			String realPath = context.getRealPath("./WEB-INF/lib/");
			File diretorio = new File(realPath);
			for (File arquivo : diretorio.listFiles()) {
				db.scanArchives(arquivo.toURI().toURL());
			}
			return db.getAnnotationIndex();
		} catch (IOException e) {
			throw new InfraestruturaException("Não foi possivel obter as entidades do classpath", e);
		}

	}

	/**
	 * cria um AnnotationDB configurado para procurar anotações de entidade
	 * 
	 * @return
	 */
	private AnnotationDB retriveConfiguredAnnotationDB() {
		AnnotationDB db = new AnnotationDB();
		db.setScanClassAnnotations(true);
		db.setScanFieldAnnotations(false);
		db.setScanMethodAnnotations(false);
		db.setScanParameterAnnotations(false);
		return db;
	}

	/**
	 * recupera o path da aplicação ou seja WEB-INF/classes
	 * 
	 * @return
	 */
	private URL retrivePathOnClasses() {
		try {
			return new File(context.getRealPath("./WEB-INF/classes")).toURI().toURL();
		} catch (Exception e) {
			throw new InfraestruturaException("Não foi possivel obter as entidades do classpath", e);
		}
	}

}
