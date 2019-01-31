package io.vilya.maia.core.resource;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Strings;

/**
 * @author erkea <erkea@vilya.io>
 */
public class DefaultClassPathResourceResolver implements ClassPathResourceResolver {

	@Override
	public List<Resource> resolve(String path) {
		if (Strings.isNullOrEmpty(path)) {
			return Collections.emptyList();
		}
		
		
		
		return null;
	}

}
