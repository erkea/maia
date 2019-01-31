package io.vilya.maia.core.resource;

import java.util.List;

/**
 * @author erkea <erkea@vilya.io>
 */
public interface ClassPathResourceResolver {

	List<Resource> resolve(String path);
	
}
