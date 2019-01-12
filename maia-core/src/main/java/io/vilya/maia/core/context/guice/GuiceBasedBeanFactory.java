/**
 * 
 */
package io.vilya.maia.core.context.guice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import io.vilya.maia.core.context.BeanFactory;
import io.vilya.maia.core.context.BeanNameGenerator;
import io.vilya.maia.core.context.BeanScanner;
import io.vilya.maia.core.context.DefaultBeanNameGenerator;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
public class GuiceBasedBeanFactory implements BeanFactory {

	private Injector injector;

	private List<Class<?>> candidates;

	private static BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

	public GuiceBasedBeanFactory(String... basePackages) {
		candidates = new BeanScanner().scan(basePackages);
	}

	@Override
	public void registerBean(Class<?> clazz) {
		// TODO The binder can only be used inside configure()
	}

	@Override
	public <T> T getBean(Class<T> clazz) {
		return injector.getInstance(clazz);
	}

	@Override
	public <T> List<T> getBeanOfType(Class<T> clazz) {
		List<Binding<T>> bindings = injector.findBindingsByType(TypeLiteral.get(clazz));
		return bindings.stream().map(binding -> binding.getProvider().get()).collect(Collectors.toList());
	}

	@Override
	public void refresh() {
		injector = createInjector(candidates);
	}

	private static Injector createInjector(List<Class<?>> candidates) {
		return Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				if (candidates != null && !candidates.isEmpty()) {
					candidates.forEach(candidate -> {
						bind(candidate).in(Scopes.SINGLETON);
						Arrays.stream(candidate.getInterfaces())
								.forEach(t -> bind(redefine(t))
										.annotatedWith(Names.named(beanNameGenerator.generate(candidate))).to(candidate)
										.in(Scopes.SINGLETON));
					});
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> redefine(Class<?> clazz) {
		return (Class<T>) clazz;
	}

}
