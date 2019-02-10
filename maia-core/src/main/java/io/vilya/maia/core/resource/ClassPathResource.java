package io.vilya.maia.core.resource;

import java.io.InputStream;

import javax.annotation.Nullable;

/**
 * @author erkea <erkea@vilya.io>
 */
public class ClassPathResource implements Resource {

	private String path;

	@Nullable
	private ClassLoader classLoader;

	public ClassPathResource(String path) {
		this.path = path;
	}

	public ClassPathResource(String path, @Nullable ClassLoader classLoader) {
		this(path);
		this.classLoader = classLoader;
	}

	@Override
	public InputStream getInputStream() {
		ClassLoader used = classLoader == null ? getClass().getClassLoader() : classLoader;
		return used.getResourceAsStream(path);
	}

}
