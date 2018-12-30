/**
 * 
 */
package io.vilya.maia.ip.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vilya.maia.ip.constant.HttpStatusCode;
import io.vilya.maia.ip.method.HandlerMethod;

/**
 * 
 * @author erkea <erkea@vilya.io>
 *
 */
public class RequestHandler implements Handler<RoutingContext> {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	private HandlerMethod handlerMethod;

	public RequestHandler(HandlerMethod handlerMethod) {
		this.handlerMethod = Preconditions.checkNotNull(handlerMethod, "HandlerMethod must not be null.");
	}

	@Override
	public void handle(RoutingContext context) {
		HttpServerRequest request = context.request();
		HttpServerResponse response = context.response();
		
		Object bean = handlerMethod.getBean();
		Method method = handlerMethod.getMethod();
		
		Object returnValue = null;
		try {
			returnValue = method.invoke(bean, request, response);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error("failed to invoke", e);
		}
		
		if (response.ended()) {
			return;
		}
		
		if (returnValue == null) {
			response.setStatusCode(HttpStatusCode.NO_CONTENT.getCode());
			response.end();
		} else {
			try {
				response.end(Buffer.buffer(objectMapper.writeValueAsBytes(returnValue)));
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
