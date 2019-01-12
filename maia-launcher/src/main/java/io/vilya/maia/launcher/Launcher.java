package io.vilya.maia.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vilya.maia.core.factory.ConfigRetrieverFactory;
import io.vilya.maia.core.factory.RouterFactory;

/**
 * 
 * @author erkea <erkea@vilya.io>
 *
 */
public class Launcher {

	private static final Logger log = LoggerFactory.getLogger(Logger.class);

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		ConfigRetriever retriever = new ConfigRetrieverFactory().create(vertx);
		retriever.getConfig(result -> {
			if (result.failed()) {
				log.error("load config failed", result.cause());
				return;
			}

			JsonObject config = result.result();
			deployVerticle(vertx, config);
		});
	}

	private static void deployVerticle(Vertx vertx, JsonObject config) {
		vertx.deployVerticle(new AbstractVerticle() {
			@Override
			public void start(Future<Void> startFuture) throws Exception {
				HttpServer server = vertx.createHttpServer();
				int port = config.getInteger("server.port", 8080);
				server.requestHandler(new RouterFactory().create(vertx)).listen(port, listenHandler -> {
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
