/**
 * 
 */
package io.vilya.maia.core.annotation;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import io.vertx.core.http.HttpMethod;

/**
 * 
 * @author erkea <erkea@vilya.io>
 *
 */
public class RequestMappingMetadata {

	private String path;
	
	private HttpMethod[] methods;
	
	private String[] consumes;
	
	private String[] produces;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public HttpMethod[] getMethods() {
		return methods;
	}

	public void setMethods(HttpMethod[] methods) {
		this.methods = methods;
	}

	public String[] getConsumes() {
		return consumes;
	}

	public void setConsumes(String[] consumes) {
		this.consumes = consumes;
	}

	public String[] getProduces() {
		return produces;
	}

	public void setProduces(String[] produces) {
		this.produces = produces;
	}

	public boolean isRegexPath() {
		return path.indexOf('*') != -1 || path.indexOf('?') != -1;
	}
	
	public static RequestMappingMetadata of(RequestMapping requestMapping) {
		Preconditions.checkNotNull(requestMapping, "RequestMapping must not be null.");
		RequestMappingMetadata metadata = new RequestMappingMetadata();
		metadata.setPath(normalizePath(requestMapping.path()));
		metadata.setMethods(requestMapping.methods());
		metadata.setConsumes(requestMapping.consumes());
		metadata.setProduces(requestMapping.produces());
		return metadata;
	}
	
	private static String normalizePath(String path) {
		if (Strings.isNullOrEmpty(path)) {
			return "";
		}
		
		String normalizedPath = path;
		if (!normalizedPath.startsWith("/")) {
			normalizedPath = "/" + normalizedPath;
		}
		
		if (normalizedPath.endsWith("/")) {
			normalizedPath = normalizedPath.substring(normalizedPath.length());
		}
		
		return normalizedPath;
	}
	
}
