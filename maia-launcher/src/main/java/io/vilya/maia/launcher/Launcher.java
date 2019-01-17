package io.vilya.maia.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vilya.maia.core.context.ApplicationContext;
import io.vilya.maia.core.context.guice.GuiceBasedApplicationContext;

/**
 * 
 * @author erkea <erkea@vilya.io>
 *
 */
public class Launcher {

	private static final Logger log = LoggerFactory.getLogger(Logger.class);

	public static void main(String[] args) {
		ApplicationContext context = new GuiceBasedApplicationContext("io.vilya.maia");
		context.refresh();
	}

}
