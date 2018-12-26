package io.vilya.maia.ip;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import io.vilya.maia.ip.constant.HttpStatusCode;
import io.vilya.maia.ip.constant.MediaType;
import io.vilya.maia.ip.domain.Accept;
import io.vilya.maia.ip.domain.Accepts;
import io.vilya.maia.ip.vo.UserInfoVO;

/**
 * 
 * @author vilya
 *
 */
public class Launcher extends AbstractVerticle {

	private static final Logger log = LoggerFactory.getLogger(Logger.class);
	
	private static final String ROOT_PATH = "/*";
		
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);
		
		router.route(ROOT_PATH).handler(rh -> {
			rh.response().putHeader("X-Vilya-Version", "1.0");
			rh.next();
		});
		
		router.route("/ip").handler(rh -> {
			HttpServerRequest request = rh.request();
			List<Accept> accepts = Accepts.of(request.getHeader(HttpHeaders.ACCEPT));
			if (accepts.isEmpty()) {
				rh.response().setStatusCode(HttpStatusCode.BAD_REQUEST.getCode());
				rh.response().end();
				return;
			}
			
			UserInfoVO userInfo = new UserInfoVO();
			userInfo.setIp(request.remoteAddress().host());
			userInfo.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
			
			
			Accept primaryAccept = accepts.get(0);
			String contentType = null;
			ObjectMapper mapper = null;
			if (primaryAccept.isCompatibleWith(MediaType.APPLICATION_JSON)) {
				contentType = MediaType.APPLICATION_JSON.getFull();
				mapper = new ObjectMapper();
			} else if (primaryAccept.isCompatibleWith(MediaType.APPLICATION_XML)
					|| primaryAccept.isCompatibleWith(MediaType.TEXT_XML)) {
				contentType = MediaType.APPLICATION_XML.getFull();
				mapper = new XmlMapper();
			} else {
				contentType = MediaType.TEXT_HTML.getFull();
				rh.put("data", userInfo);
				rh.reroute("/ip/index.html");
				return;
			}
			
			if (contentType != null) {
				rh.response().putHeader(HttpHeaders.CONTENT_TYPE, contentType);
				try {
					rh.response().end(mapper.writeValueAsString(userInfo));
				} catch (JsonProcessingException e) {
					log.error("", e);
					rh.response().setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode());
					rh.response().end();
				}				
			} else {
				rh.response().setStatusCode(HttpStatusCode.NOT_IMPLEMENTED.getCode());
				rh.response().end();
			}
			
		});
		
		router.getWithRegex(".+\\.html").handler(TemplateHandler.create(ThymeleafTemplateEngine.create(vertx)));
		
		router.route(ROOT_PATH).failureHandler(fh -> {
			fh.response().setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode()).end("Sorry!");
		});
		
		server.requestHandler(router).listen(8080);
	}
	
	

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Launcher());
	}
	
}
