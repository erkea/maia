/**
 * 
 */
package io.vilya.maia.core.context;

import java.util.List;

import io.vertx.core.Vertx;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
public interface ApplicationContext {

	Vertx vertx();
	
	void registerBean(Class<?> clazz);
	
	<T> T getBean(Class<T> clazz);
	
	<T> List<T> getBeanOfType(Class<T> clazz);
	
	List<Class<?>> getCandidates();
	
	void refresh();

}
