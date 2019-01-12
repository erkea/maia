/**
 * 
 */
package io.vilya.maia.core.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
import io.vilya.maia.core.constant.HttpStatusCode;
import io.vilya.maia.core.exception.MaiaRuntimeException;
import io.vilya.maia.core.method.HandlerMethod;

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
		
		if (response.ended()) {
			return;
		}
		
		Object bean = handlerMethod.getBean();
		Method method = handlerMethod.getMethod();
				
		Object returnValue = null;
		try {
			returnValue = method.invoke(bean, fillMethodArgs(method, request, response));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error("failed to invoke", e);
		}
		
		if (returnValue == null) {
			response.setStatusCode(HttpStatusCode.NO_CONTENT.getCode());
			response.end();
		} else {
			try {
				response.end(Buffer.buffer(objectMapper.writeValueAsBytes(returnValue)));
			} catch (JsonProcessingException e) {
				throw new MaiaRuntimeException(e);
			}
		}
	}
	
	private static Object[] fillMethodArgs(Method method, HttpServerRequest request, HttpServerResponse response) {
		List<Object> args = new ArrayList<>(method.getParameterCount());
		for (Class<?> type : method.getParameterTypes()) {			
			if (HttpServerRequest.class.isAssignableFrom(type)) {
				args.add(request);
			} else if (HttpServerResponse.class.isAssignableFrom(type)) {
				args.add(response);
			} else {
				throw new MaiaRuntimeException("Unsupported parameter type: " + type);				
			}
		}
		
		return args.toArray();
	}

}
