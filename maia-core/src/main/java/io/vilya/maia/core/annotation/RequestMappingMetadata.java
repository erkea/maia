/**
 * 
 */
package io.vilya.maia.core.annotation;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import io.vertx.core.http.HttpMethod;

/**
 * 
 * {@link RequestMapping}
 * @author erkea <erkea@vilya.io>
 */
public class RequestMappingMetadata {

	private static final RequestMappingMetadata EMPTY = createEmpty();
			
	private String path;
	
	private HttpMethod[] methods;
	
	private String[] consumes;
	
	private String[] produces;
	
	public String getPath() {
		return path;
	}

	public HttpMethod[] getMethods() {
		return methods;
	}

	public String[] getConsumes() {
		return consumes;
	}

	public String[] getProduces() {
		return produces;
	}

	public boolean isRegexPath() {
		return path.indexOf('*') != -1 || path.indexOf('?') != -1;
	}
	
	public static RequestMappingMetadata of(RequestMapping requestMapping) {
		if (requestMapping == null) {
			return EMPTY;
		}
		
		Preconditions.checkNotNull(requestMapping);
		RequestMappingMetadata metadata = new RequestMappingMetadata();
		metadata.path = normalizePath(requestMapping.path());
		metadata.methods = requestMapping.methods();
		metadata.consumes = requestMapping.consumes();
		metadata.produces = requestMapping.produces();
		return metadata;
	}
	
	public static RequestMappingMetadata merge(RequestMappingMetadata rmm1, RequestMappingMetadata rmm2) {
		Preconditions.checkNotNull(rmm1);
		Preconditions.checkNotNull(rmm2);
		
		RequestMappingMetadata merged = new RequestMappingMetadata();
		merged.path = normalizePath(rmm1.path) + normalizePath(rmm2.path);
		merged.methods = merge(rmm1.methods, rmm2.methods);
		merged.consumes = merge(rmm1.consumes, rmm2.consumes);
		merged.produces = merge(rmm1.produces, rmm2.produces);
		return merged;
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
	
	@SafeVarargs
	private static <T> T[] merge(T[] first, T[]... other) {
		Builder<T> merged = ImmutableSet.builder();
		merged.add(first);
		for (T[] ts : other) {
			merged.add(ts);
		}
		return merged.build().toArray(first);
	}
	
	private static RequestMappingMetadata createEmpty() {
		RequestMappingMetadata metadata = new RequestMappingMetadata();
		metadata.path = "";
		metadata.methods = new HttpMethod[0];
		metadata.consumes = new String[0];
		metadata.produces = new String[0];
		return metadata;
	}
}
