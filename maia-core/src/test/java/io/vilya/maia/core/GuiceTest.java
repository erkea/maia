/**
 * 
 */
package io.vilya.maia.core;

import org.junit.jupiter.api.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
public class GuiceTest {

	@Test
	public void test1() {
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(Foo.class);
			}
			
		});
		
		injector.getInstance(Foo.class).print();
	}
	
	private static class Foo {
		
		public void print() {
			System.out.println("Hello world.");
		}
		
	}
	
	
}
