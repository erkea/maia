/**
 * 
 */
package io.vilya.maia.core.context.guice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vilya.maia.core.context.ApplicationContext;
import io.vilya.maia.core.context.BeanNameGenerator;
import io.vilya.maia.core.context.BeanScanner;
import io.vilya.maia.core.context.DefaultBeanNameGenerator;
import io.vilya.maia.core.factory.ConfigRetrieverFactory;
import io.vilya.maia.core.factory.RouterFactory;

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

	private static BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

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

	@Override
	public <T> T getBean(Class<T> clazz) {
		return injector.getInstance(clazz);
	}

	@Override
	public <T> List<T> getBeanOfType(Class<T> clazz) {
		Preconditions.checkNotNull(clazz);
		List<Binding<T>> bindings = injector.findBindingsByType(TypeLiteral.get(clazz));
		return bindings.stream().map(binding -> binding.getProvider().get()).collect(Collectors.toList());
	}
	
	@Override
	public List<Class<?>> getCandidates() {
		return candidates;
	}

	@Override
	public void refresh() {
		refreshInjector();
		deployVerticles();
	}
	
	private void refreshInjector() {
		injector = createInjector(candidates);
	}

	private static Injector createInjector(List<Class<?>> candidates) {
		return Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				if (candidates == null || candidates.isEmpty()) {
					return;
				}

				candidates.forEach(candidate -> {
					bind(candidate).in(Scopes.SINGLETON);
					Arrays.stream(candidate.getInterfaces())
							.forEach(t -> bind(generics(t))
									.annotatedWith(Names.named(beanNameGenerator.generate(candidate))).to(candidate)
									.in(Scopes.SINGLETON));
				});
			}
		});
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

	@SuppressWarnings("unchecked")
	private static <T> Class<T> generics(Class<?> clazz) {
		return (Class<T>) clazz;
	}
	
	private static List<Binding<?>> getAllBindings(Injector injector) {
		List<Binding<?>> bindings = new ArrayList<>();
		injector.getAllBindings().forEach((key, value) -> bindings.add(value));
		return bindings;
	}
	
	
	
}
