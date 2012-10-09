package br.com.aexo.sim.seguranca.autenticacao;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.jboss.seam.security.annotations.SecurityBindingType;

@SecurityBindingType
@Target({ TYPE, METHOD, PARAMETER, FIELD })
@Retention(RUNTIME)
@Qualifier
public @interface Autenticado {

}
