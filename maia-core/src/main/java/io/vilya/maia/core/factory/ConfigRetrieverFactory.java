package io.vilya.maia.core.factory;

import com.google.common.base.Preconditions;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;
import io.vilya.maia.core.context.ApplicationContext;

/**
 *
 * @author erkea <erkea@vilya.io>
 *
 */
public class ConfigRetrieverFactory implements VertxComponentFactory<ConfigRetriever> {

    @Override
    public ConfigRetriever create(ApplicationContext context) {
        Preconditions.checkNotNull(context, "ApplicationContext required.");
        ConfigStoreOptions configStoreOptions = new ConfigStoreOptions()
                .setType("file")
                .setFormat("properties")
                .setConfig(new JsonObject().put("path", "application.properties"));
        ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions();
        configRetrieverOptions.addStore(configStoreOptions);
        return ConfigRetriever.create(context.vertx(), configRetrieverOptions);
    }

}
