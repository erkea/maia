/**
 * 
 */
package io.vilya.maia.core.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.function.Predicate;

import io.vilya.maia.core.util.AnnotationUtils;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
@FunctionalInterface
public interface ClassFilter extends Predicate<Class<?>> {

	@Override
	boolean test(Class<?> t);

	default ClassFilter and(ClassFilter other) {
		Objects.requireNonNull(other);
        return t -> test(t) && other.test(t);
	}



	static ClassFilter ofAnnotation(Class<? extends Annotation> annotation) {
		return clazz -> AnnotationUtils.hasAnnotation(clazz, annotation);
	}

	static ClassFilter isClass() {
		return clazz -> {
			if (clazz == null) {
				return false;
			}

			if (clazz.isAnnotation() || clazz.isArray() || clazz.isEnum() || clazz.isInterface()) {
				return false;
			}

			if (Modifier.isAbstract(clazz.getModifiers())) {
				return false;
			}

			return true;
		};
	}

}
