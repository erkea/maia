/**
 * 
 */
package io.vilya.maia.core.context;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
public interface BeanNameGenerator {

	String generate(Class<?> bean);
	
}
