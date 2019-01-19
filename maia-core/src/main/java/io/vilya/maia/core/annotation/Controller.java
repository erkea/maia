package io.vilya.maia.core.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * marker for controller layer component
 * @author erkea <erkea@vilya.io>
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
@Component
public @interface Controller {}
