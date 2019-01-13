package io.vilya.maia.core.factory;

import io.vilya.maia.core.context.ApplicationContext;

/**
 *
 * @author erkea <erkea@vilya.io>
 *
 */
public interface VertxComponentFactory<T> {

    T create(ApplicationContext context);

}
