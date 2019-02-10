/**
 * 
 */
package io.vilya.maia.core.context;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import io.vilya.maia.core.annotation.Component;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
public class BeanScanner {

	private static final Class<? extends Annotation> DEFAULT_ANNOTATION = Component.class;

	private Class<? extends Annotation> annotation;

	private ClassFilter classFilter;

	public BeanScanner() {
		this(DEFAULT_ANNOTATION);
	}
	
	public BeanScanner(Class<? extends Annotation> annotation) {
		this.annotation = annotation;
		this.classFilter = ClassFilter.isClass().and(ClassFilter.ofAnnotation(annotation));
	}

	public List<Class<?>> scan(String... basePackages) {
		return scan(BeanScanner.class.getClassLoader(), basePackages);
	}

	public List<Class<?>> scan(ClassLoader classLoader, String... basePackages) {
		if (basePackages.length == 0) {
			return Collections.emptyList();
		}

		return Arrays.stream(basePackages).flatMap(basePackage -> {
			try {
				return scanLazily(classLoader, basePackage);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}).collect(Collectors.toList());
	}

	private Stream<? extends Class<?>> scanLazily(ClassLoader classLoader, String basePackage) throws IOException {
		Preconditions.checkNotNull(classLoader, "ClassLoader must not be null.");

		if (Strings.isNullOrEmpty(basePackage)) {
			return Stream.of();
		}

		ClassPath classPath = ClassPath.from(classLoader);
		ImmutableSet<ClassInfo> classInfos = classPath.getTopLevelClassesRecursive(basePackage);
		return classInfos.stream()
				.map(ClassInfo::load)
				.flatMap(this::includeAllClasses)
				.filter(classFilter);
	}
	
	private Stream<? extends Class<?>> includeAllClasses(Class<?> clazz) {
		if (clazz == null) {
			return Stream.of();
		}
		
		List<Class<?>> all = new ArrayList<>();
		all.add(clazz);
		includeDeclaredClasses(clazz, all);
		return all.stream();
	}
	
	private void includeDeclaredClasses(Class<?> clazz, List<Class<?>> declaredClasses) {
		Arrays.stream(clazz.getDeclaredClasses()).filter(classFilter).forEach(t -> {
			declaredClasses.add(t);
			includeDeclaredClasses(t, declaredClasses);
		});
	}

}
