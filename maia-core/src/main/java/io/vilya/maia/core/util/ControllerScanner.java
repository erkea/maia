/**
 * 
 */
package io.vilya.maia.core.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import io.vilya.maia.core.annotation.Controller;

/**
 * 
 * @author erkea <erkea@vilya.io>
 *
 */
public class ControllerScanner {

	private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);
	
	private ControllerScanner() {}
	
	public static List<Class<?>> scanQuietly(String basePackageName) {
		try {
			return scan(ControllerScanner.class.getClassLoader(), basePackageName);			
		} catch (IOException e) {
			log.error("failed to scan: pachageName=" + basePackageName, e);
			return Collections.emptyList();
		}
	}
	
	public static List<Class<?>> scan(String basePackageName) throws IOException {
		return scan(ControllerScanner.class.getClassLoader(), basePackageName);
	}
	
	public static List<Class<?>> scan(ClassLoader classLoader, String basePackageName) throws IOException {
		return scanLazily(classLoader, basePackageName)
				.collect(Collectors.toList());
	}
	
	public static Stream<? extends Class<?>> scanLazily(String basePackageName) throws IOException {
		return scanLazily(ControllerScanner.class.getClassLoader(), basePackageName);
	}
	
	public static Stream<? extends Class<?>> scanLazily(ClassLoader classLoader, String basePackageName) throws IOException {
		Preconditions.checkNotNull(classLoader, "ClassLoader must not be null.");
		
		if (Strings.isNullOrEmpty(basePackageName)) {
			return Stream.of();
		}
		
		ClassPath classPath = ClassPath.from(classLoader);
		ImmutableSet<ClassInfo> classInfos = classPath.getTopLevelClassesRecursive(basePackageName);
		return classInfos.stream()
				.map(ClassInfo::load)
				.filter(hasControllerAnnotation());
	}
	
	private static Predicate<Class<?>> hasControllerAnnotation() {
		return clazz -> clazz != null && clazz.isAnnotationPresent(Controller.class);
	}
	
}
