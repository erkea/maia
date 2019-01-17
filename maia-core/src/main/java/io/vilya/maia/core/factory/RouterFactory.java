package io.vilya.maia.core.factory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vilya.maia.core.annotation.Component;
import io.vilya.maia.core.annotation.Controller;
import io.vilya.maia.core.annotation.RequestMapping;
import io.vilya.maia.core.annotation.RequestMappingMetadata;
import io.vilya.maia.core.constant.HttpStatusCode;
import io.vilya.maia.core.context.ApplicationContext;
import io.vilya.maia.core.context.ApplicationContextAware;
import io.vilya.maia.core.context.ClassFilter;
import io.vilya.maia.core.handler.RequestHandler;
import io.vilya.maia.core.handler.RoutingHandler;
import io.vilya.maia.core.method.HandlerMethod;

/**
 *
 * @author erkea <erkea@vilya.io>
 *
 */
@Component
public class RouterFactory implements VertxComponentFactory<Router> {

    private static final Logger log = LoggerFactory.getLogger(RouterFactory.class);
    
	private ApplicationContext ctx;
        
    @Override
    public Router create(ApplicationContext context) {
    	Preconditions.checkNotNull(context, "ApplicationContext required.");
        Router router = Router.router(context.vertx());
        bindDefaulthandlers(router);
        bindRequestHandlers(context, router);
        return router;
    }

    private static void bindDefaulthandlers(Router router) {
        router.route(RoutingHandler.ROOT_PATH).handler(LoggerHandler.create());

        router.route(RoutingHandler.ROOT_PATH).handler(rh -> {
            rh.response().putHeader("X-Vilya-Version", "1.0");
            rh.next();
        });

        router.route(RoutingHandler.ROOT_PATH).order(Integer.MAX_VALUE - 10).handler(StaticHandler.create("static"));

        router.route(RoutingHandler.ROOT_PATH).last().failureHandler(rh -> {
            rh.response().setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode());
            rh.response().end();
        });
    }

    public static void bindRequestHandlers(ApplicationContext context, Router router) {
        List<Class<?>> classes = context.getCandidates();
        classes.stream()
        	.filter(ClassFilter.ofAnnotation(Controller.class))
        	.flatMap(clazz -> collectMetadata(context, clazz))
        	.forEach(handlerMethod -> {
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
	
	            log.info("{}", Json.encodePrettily(metadata));
	            route.handler(new RequestHandler(handlerMethod));
        	});
    }

    private static Stream<HandlerMethod> collectMetadata(ApplicationContext context, Class<?> clazz) {
        Object instance = context.getBean(clazz);
        if (instance == null) {
            return Stream.of();
        }

        final Object finalInstance = instance;
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(t -> t.isAnnotationPresent(RequestMapping.class))
                .map(t -> {
                	RequestMappingMetadata classMetadata = 
                			RequestMappingMetadata.of(clazz.getAnnotation(RequestMapping.class));
                	RequestMappingMetadata methodMetadata = 
                			RequestMappingMetadata.of(t.getAnnotation(RequestMapping.class));
                	
                    HandlerMethod handlerMethod = new HandlerMethod();
                    handlerMethod.setBean(finalInstance);
                    handlerMethod.setMethod(t);
                    handlerMethod.setMetadata(RequestMappingMetadata.merge(classMetadata, methodMetadata));
                    return handlerMethod;
                });
    }

}
