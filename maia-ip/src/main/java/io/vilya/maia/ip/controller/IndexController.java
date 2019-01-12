/**
 * 
 */
package io.vilya.maia.ip.controller;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vilya.maia.core.annotation.Controller;
import io.vilya.maia.core.annotation.RequestMapping;
import io.vilya.maia.core.util.RequestUtils;
import io.vilya.maia.ip.vo.UserInfoVO;

/**
 * 
 * @author erkea <erkea@vilya.io>
 *
 */
@Controller
@RequestMapping(path="/demo")
public class IndexController {

	@RequestMapping(path="index")
	public UserInfoVO index(HttpServerRequest request, HttpServerResponse response) {
		UserInfoVO userInfo = new UserInfoVO();
		userInfo.setIp(RequestUtils.getRealIp(request));
		userInfo.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		return userInfo;
	}
	
}
