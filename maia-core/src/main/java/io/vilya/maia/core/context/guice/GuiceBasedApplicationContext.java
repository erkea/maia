/**
 * 
 */
package io.vilya.maia.core.context.guice;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableListMultimap.Builder;
import com.google.common.collect.ListMultimap;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vilya.maia.core.context.ApplicationContext;
import io.vilya.maia.core.context.ApplicationContextAware;
import io.vilya.maia.core.context.BeanScanner;
import io.vilya.maia.core.factory.ConfigRetrieverFactory;
import io.vilya.maia.core.factory.RouterFactory;
import io.vilya.maia.core.util.ClassUtils;

/**
 * @author erkea <erkea@vilya.io>
 * TODO cache bean from guice
 * TODO generic
 */
public class GuiceBasedApplicationContext implements ApplicationContext {

	private static final Logger log = LoggerFactory.getLogger(GuiceBasedApplicationContext.class);

	private Vertx vertx;

	private ConfigRetriever configRetriever;

	private Injector injector;

	private List<Class<?>> candidates;

	private ListMultimap<Class<?>, Object> configured = ImmutableListMultimap.of();

	public GuiceBasedApplicationContext(String... basePackages) {
		candidates = new BeanScanner().scan(basePackages);
		vertx = Vertx.vertx();
		configRetriever = new ConfigRetrieverFactory().create(this);
	}

	@Override
	public Vertx vertx() {
		return vertx;
	}

	@Override
	public void registerBean(Class<?> clazz) {
		// TODO The binder can only be used inside configure()
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(Class<T> clazz) {
		List<Object> instances = configured.get(clazz);
		return instances.isEmpty() ? null : (T) instances.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getBeanOfType(Class<T> clazz) {
		return (List<T>) configured.get(clazz);
	}

	@Override
	public List<Class<?>> getCandidates() {
		return candidates;
	}

	@Override
	public void refresh() {
		refreshInjector();
		configureBeans();
		deployVerticles();
	}

	private void configureBeans() {
		Builder<Class<?>, Object> builder = ImmutableListMultimap.builder();
		candidates.forEach(candidate -> {
			List<Binding<Object>> bindings = 
					injector.findBindingsByType(TypeLiteral.get(ClassUtils.generics(candidate)));
			if (bindings != null && !bindings.isEmpty()) {
				List<Object> instances = bindings.stream()
						.map(binding -> binding.getProvider().get())
						.collect(Collectors.toList());
				
				instances.forEach(instance -> {
					invokeAware(instance);
					builder.put(candidate, instance);	
				});
			}
		});
		configured = builder.build();
	}
	
	private void invokeAware(Object instance) {
		if (instance instanceof ApplicationContextAware) {
			((ApplicationContextAware) instance).setApplicationContext(this);
		}
	}

	private void refreshInjector() {
		injector = GuiceInjectorFactory.createInjector(candidates);
	}
	
	private void deployVerticles() {
		configRetriever.getConfig(result -> {
			if (result.failed()) {
				log.error("load config failed", result.cause());
				return;
			}

			JsonObject config = result.result();
			deployVerticle(vertx, config);
		});
	}

	private void deployVerticle(Vertx vertx, JsonObject config) {
		ApplicationContext applicationContext = this;
		vertx.deployVerticle(new AbstractVerticle() {
			@Override
			public void start(Future<Void> startFuture) throws Exception {
				RouterFactory routerFactory = getBean(RouterFactory.class);
				if (routerFactory == null) {
					startFuture.fail("RouterFactory must be provided.");
					return;
				}

				HttpServer server = vertx.createHttpServer();
				int port = config.getInteger("server.port", 8080);
				server.requestHandler(routerFactory.create(applicationContext)).listen(port, listenHandler -> {
					if (listenHandler.succeeded()) {
						startFuture.complete();
					} else {
						startFuture.fail(listenHandler.cause());
					}
				});
			}
		});
	}

}
