package br.com.aexo.util.hibernate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * marca um parametro para ser carregado automaticamente pelo banco para
 * funcionar deve ter o parameterLoaderInterceptor carregado.
 * 
 * @author carlosr
 * 
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Load {

}
