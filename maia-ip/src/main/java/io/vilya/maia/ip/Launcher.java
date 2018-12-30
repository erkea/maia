package io.vilya.maia.ip;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vilya.maia.ip.annotation.RequestMapping;
import io.vilya.maia.ip.annotation.RequestMappingMetadata;
import io.vilya.maia.ip.constant.HttpStatusCode;
import io.vilya.maia.ip.handler.RequestHandler;
import io.vilya.maia.ip.method.HandlerMethod;
import io.vilya.maia.ip.util.ControllerScanner;

/**
 * 
 * @author erkea <erkea@vilya.io>
 *
 */
public class Launcher {

	private static final Logger log = LoggerFactory.getLogger(Logger.class);

	private static final String ROOT_PATH = "/*";

	public static void main(String[] args) {
		List<Class<?>> classes = ControllerScanner.scanQuietly(Launcher.class.getPackageName());

		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new AbstractVerticle() {
			@Override
			public void start(Future<Void> startFuture) throws Exception {
				HttpServer server = vertx.createHttpServer();
				Router router = Router.router(vertx);

				router.route(ROOT_PATH).handler(rh -> {
					log.debug("REQUEST URI: {}", rh.request().uri());
					log.debug("REQUEST HOST: {}", rh.request().host());
					rh.next();
				});
				
				router.route(ROOT_PATH).handler(rh -> {
					rh.response().putHeader("X-Vilya-Version", "1.0");
					rh.next();
				});

				router.route(ROOT_PATH).last().failureHandler(rh -> {
					log.error("INTERNAL_SERVER_ERROR", rh.failure());
					rh.response().setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode());
					rh.response().end();
				});

				classes.stream().flatMap(Launcher::collectMetadata).forEach(handlerMethod -> {
					RequestMappingMetadata metadata = handlerMethod.getMetadata();

					Route route = metadata.isRegexPath() ? router.routeWithRegex(metadata.getPath())
							: router.route(metadata.getPath());

					for (HttpMethod method : metadata.getMethods()) {
						route.method(method);
					}

					for (String consume : metadata.getConsumes()) {
						route.consumes(consume);
					}

					for (String produce : metadata.getProduces()) {
						route.produces(produce);
					}

					route.handler(new RequestHandler(handlerMethod));

					log.debug("ROUTE INFO: {}", route.toString());
				});

				server.requestHandler(router).listen(8080, listenHandler -> {
					if (listenHandler.succeeded()) {
						startFuture.complete();
					} else {
						startFuture.fail(listenHandler.cause());
					}
				});
			}
		});

	}

	private static Stream<HandlerMethod> collectMetadata(Class<?> clazz) {
		Object instance = null;
		try {
			instance = clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			log.error("", e);
		}

		if (instance == null) {
			return Stream.of();
		}

		final Object finalInstance = instance;
		return Arrays.stream(clazz.getDeclaredMethods())
				.filter(t -> t.isAnnotationPresent(RequestMapping.class))
				.map(t -> {
					HandlerMethod handlerMethod = new HandlerMethod();
					handlerMethod.setBean(finalInstance);
					handlerMethod.setMethod(t);
					handlerMethod.setMetadata(RequestMappingMetadata.of(t.getAnnotation(RequestMapping.class)));
					return handlerMethod;
				});
	}

}
