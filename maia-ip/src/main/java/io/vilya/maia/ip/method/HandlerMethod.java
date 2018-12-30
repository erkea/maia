/**
 * 
 */
package io.vilya.maia.ip.method;

import java.lang.reflect.Method;

import io.vilya.maia.ip.annotation.RequestMappingMetadata;

/**
 * 
 * @author erkea <erkea@vilya.io>
 *
 */
public class HandlerMethod {

	private Object bean;
	
	private Method method;
	
	private RequestMappingMetadata metadata;

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public RequestMappingMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(RequestMappingMetadata metadata) {
		this.metadata = metadata;
	}
	
}
