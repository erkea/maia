/**
 * 
 */
package io.vilya.maia.core.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
public class AnnotationUtils {

	private AnnotationUtils() {}
	
	public static Set<Annotation> getAnnotations(Class<?> clazz) {
		Preconditions.checkNotNull(clazz);
		Builder<Annotation> builder = ImmutableSet.builder();
		new AnnotationMetadata(clazz).accept(new AnnotationVisitor() {
			@Override
			public void visit(Annotation annotation) {
				builder.add(annotation);
			}
		});
		return builder.build();
	}
	
	public static Set<Class<? extends Annotation>> getAnnotationTypes(Class<?> clazz) {
		Preconditions.checkNotNull(clazz);
		Builder<Class<? extends Annotation>> builder = ImmutableSet.builder();
		new AnnotationMetadata(clazz).accept(new AnnotationVisitor() {
			@Override
			public void visitType(Class<? extends Annotation> type) {
				builder.add(type);
			}
		});
		return builder.build();
	}
	
	public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationType) {
		return getAnnotationTypes(clazz).contains(annotationType);
	}
	
	private static class AnnotationMetadata {
		
		private Set<Class<? extends Annotation>> visitedTypes;
		
		private Class<?> clazz;
		
		public AnnotationMetadata(Class<?> clazz) {
			this.visitedTypes = new HashSet<>();
			this.clazz = clazz;
		}
		
		public void accept(AnnotationVisitor visitor) {
			visitAnnotations(clazz, visitor);
		}
		
		private void visitAnnotations(Class<?> clazz, AnnotationVisitor visitor) {
			Arrays.stream(clazz.getAnnotations()).forEach(annotation -> {
				visitor.visit(annotation);
				if (visitedTypes.add(annotation.annotationType())) {
					visitor.visitType(annotation.annotationType());
					visitAnnotations(annotation.annotationType(), visitor);				
				}
			});				
		}
	}
	
	private interface AnnotationVisitor {
		
		default void visit(Annotation annotation) {}
		
		default void visitType(Class<? extends Annotation> type) {}
		
	}
	
}
