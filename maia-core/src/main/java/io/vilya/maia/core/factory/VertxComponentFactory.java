package io.vilya.maia.core.factory;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;

/**
 *
 * @author erkea <erkea@vilya.io>
 *
 */
public interface VertxComponentFactory<T> {

    T create(Vertx vertx);

}
