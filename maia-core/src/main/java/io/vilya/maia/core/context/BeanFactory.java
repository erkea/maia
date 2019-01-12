/**
 * 
 */
package io.vilya.maia.core.context;

import java.util.List;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
public interface BeanFactory {

	void registerBean(Class<?> clazz);
	
	<T> T getBean(Class<T> clazz);
	
	<T> List<T> getBeanOfType(Class<T> clazz);
	
	void refresh();
	
}
