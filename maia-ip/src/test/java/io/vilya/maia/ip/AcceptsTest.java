package io.vilya.maia.ip;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.Json;
import io.vilya.maia.ip.domain.Accepts;

/**
 * 
 * @author vilya
 *
 */
public class AcceptsTest {

	private static final Logger log = LoggerFactory.getLogger(AcceptsTest.class);
	
	@Test
	public void of1() {
		String accept = "text/*, text/html, text/html;level=1, */*";
		log.info("{}", Json.encodePrettily(Accepts.of(accept)));
	}
	
	@Test
	public void of2() {
		String accept = "text/*;q=0.3, text/html;q=0.7, text/html;level=1,text/html;level=2;q=0.4, */*;q=0.5";
		log.info("{}", Json.encodePrettily(Accepts.of(accept)));
	}
	
	@Test
	public void ofaOrdered() {
		String accept = "text/*;q=0.3, text/html;q=0.7, text/html;level=1,text/html;level=2;q=0.4, */*;q=0.5";
		log.info("{}", Json.encodePrettily(Accepts.ofOrdered(accept)));
	}
	
}
