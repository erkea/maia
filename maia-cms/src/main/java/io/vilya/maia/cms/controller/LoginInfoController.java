/**
 * 
 */
package io.vilya.maia.cms.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vilya.common.api.RestResponse;
import io.vilya.maia.cms.service.LoginInfoService;
import io.vilya.maia.core.annotation.Controller;
import io.vilya.maia.core.annotation.RequestMapping;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
@Controller
@RequestMapping(path = "api/cms/li")
public class LoginInfoController {

	private static final Logger log = LoggerFactory.getLogger(LoginInfoController.class);
	
	@Inject
	@Named("loginInfoServiceImpl")
	private LoginInfoService loginInfoService;
	
	@RequestMapping(path = "index")
	public RestResponse index() {
		return new RestResponse();
	}
	
}
