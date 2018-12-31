/**
 * 
 */
package io.vilya.maia.ip.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.vertx.core.http.HttpMethod;

/**
 * 
 * @author erkea <erkea@vilya.io>
 *
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface RequestMapping {

	String path() default "";
	
	HttpMethod[] methods() default {};
	
	String[] consumes() default {};
	
	String[] produces() default {};
	
}
