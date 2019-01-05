package io.vilya.maia.ip.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.Objects;

/**
 *
 * @author erkea <erkea@vilya.io>
 *
 */
public interface RoutingHandler {

    String ROOT_PATH = "/*";
    
    int getOrder();

    Handler<RoutingContext> getHandler();
    
    String getPath();
    
    default boolean isRoot() {
        return Objects.equals(ROOT_PATH, getPath());
    }
}
