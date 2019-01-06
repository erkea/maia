package io.vilya.maia.ip.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * @author erkea <erkea@vilya.io>
 *
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface ResponseBody {
}
