/**
 * 
 */
package io.vilya.maia.core.context;

import java.util.function.Predicate;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
@FunctionalInterface
public interface ClassFilter extends Predicate<Class<?>> {

	@Override
	boolean test(Class<?> t);

}
