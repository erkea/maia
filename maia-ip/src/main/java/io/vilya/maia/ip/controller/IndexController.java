/**
 * 
 */
package io.vilya.maia.ip.controller;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vilya.maia.ip.annotation.Controller;
import io.vilya.maia.ip.annotation.RequestMapping;
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
		userInfo.setIp(request.remoteAddress().host());
		userInfo.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		return userInfo;
	}
	
}