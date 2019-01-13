package io.vilya.maia.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

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
		Stopwatch stopwatch = Stopwatch.createStarted();
		ApplicationContext context = new GuiceBasedApplicationContext("io.vilya.maia");
		context.refresh();
		stopwatch.stop();
		log.info("{}", stopwatch.elapsed());
	}

}
