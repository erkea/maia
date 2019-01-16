/**
 * 
 */
package io.vilya.maia.cms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vilya.common.api.RestResponse;
import io.vilya.maia.core.annotation.Controller;
import io.vilya.maia.core.annotation.RequestMapping;
import io.vilya.maia.core.context.ApplicationContext;
import io.vilya.maia.core.context.ApplicationContextAware;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
@Controller
public class LoginInfoController implements ApplicationContextAware {

	private static final Logger log = LoggerFactory.getLogger(LoginInfoController.class);
	
	private ApplicationContext ctx;
	
	@RequestMapping(path = "/api/li")
	public RestResponse loginInfo() {
		log.info("ctx: {}", ctx.getClass());
		return new RestResponse();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		this.ctx = ctx;
	}
	
}
