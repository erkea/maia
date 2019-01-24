package io.vilya.maia.core.util;

/**
 * @author erkea <erkea@vilya.io>
 */
public class ClassUtils {

	private ClassUtils() {}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> generics(Class<?> clazz) {
		return (Class<T>) clazz;
	}
	
}
