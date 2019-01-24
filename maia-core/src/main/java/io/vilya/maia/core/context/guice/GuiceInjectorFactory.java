package io.vilya.maia.core.context.guice;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

import io.vilya.maia.core.context.BeanNameGenerator;
import io.vilya.maia.core.context.DefaultBeanNameGenerator;
import io.vilya.maia.core.exception.MaiaRuntimeException;
import io.vilya.maia.core.util.AnnotationUtils;
import io.vilya.maia.core.util.ClassUtils;

/**
 * @author erkea <erkea@vilya.io>
 */
public class GuiceInjectorFactory {

	private static BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();
	
	private GuiceInjectorFactory() {}
	
	public static Injector createInjector(List<Class<?>> candidates) {
		return Guice.createInjector(new GenericModule(candidates));
	}
	
	private static class GenericModule extends AbstractModule {
		
		private Set<Class<?>> noNameProvided = new HashSet<>();
		
		private List<Class<?>> candidates;
		
		public GenericModule(List<Class<?>> candidates) {
			this.candidates = candidates;
		}
		
		@Override
		protected void configure() {
			if (candidates == null || candidates.isEmpty()) {
				return;
			}

			candidates.forEach(candidate -> {
				// bind self
				bind(candidate).in(Scopes.SINGLETON);
				// bind interfaces
				boolean named = AnnotationUtils.hasAnnotation(candidate, Named.class);
				Arrays.stream(candidate.getInterfaces())
						.forEach(t -> {
							if (!named) {
								if (!noNameProvided.add(t)) {
									throw new MaiaRuntimeException("can not bind same interface to more than one unnamed instances.");									
								}
								
								bind(ClassUtils.generics(t)).to(candidate).in(Scopes.SINGLETON);
							}
							
							bind(ClassUtils.generics(t))
								.annotatedWith(Names.named(beanNameGenerator.generate(candidate))).to(candidate)
								.in(Scopes.SINGLETON);
						});
			});
			
			noNameProvided.clear();
		}
	}
	
}
