package io.vilya.maia.ip.factory;

import com.google.common.base.Preconditions;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author erkea <erkea@vilya.io>
 *
 */
public class ConfigRetrieverFactory implements VertxComponentFactory<ConfigRetriever> {

    @Override
    public ConfigRetriever create(Vertx vertx) {
        Preconditions.checkNotNull(vertx, "Vertx required.");
        ConfigStoreOptions configStoreOptions = new ConfigStoreOptions()
                .setType("file")
                .setFormat("properties")
                .setConfig(new JsonObject().put("path", "application.properties"));
        ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions();
        configRetrieverOptions.addStore(configStoreOptions);
        return ConfigRetriever.create(vertx, configRetrieverOptions);
    }

}
