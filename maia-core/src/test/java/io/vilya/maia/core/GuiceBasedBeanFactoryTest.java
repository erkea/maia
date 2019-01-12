/**
 * 
 */
package io.vilya.maia.core;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import io.vilya.maia.core.annotation.Component;
import io.vilya.maia.core.context.BeanFactory;
import io.vilya.maia.core.context.guice.GuiceBasedBeanFactory;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
@Component
public class GuiceBasedBeanFactoryTest {

	private static BeanFactory componentFactory;
	
	private static Injector injector;
	
	@BeforeAll
	public static void init() {
		componentFactory = new GuiceBasedBeanFactory("io.vilya.maia.core");
		componentFactory.refresh();
		
		injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				super.configure();
			}
		});
	}
	
	@Test
	public void test1() {
		componentFactory.getBeanOfType(Foo.class).forEach(System.out::println);
	}
	
	@Test
	public void test2() {
		componentFactory.getBeanOfType(FooImpl1.class).forEach(System.out::println);
	}
	
	@Test
	public void test3() {
		componentFactory.getBeanOfType(Foo.class).forEach(System.out::println);
	}
	
	@Test
	public void test4() {
		System.out.println(componentFactory.getBean(Foo.class));
	}
	
	@Test
	public void test6() {
		Arrays.toString(String.class.getInterfaces());
	}
	
	@Test
	public void test5() {
		componentFactory.getBeanOfType(Foo.class).forEach(System.out::println);
		componentFactory.getBean(Bar.class).test();
	}
	
	private interface Foo {}
	
	@Component
	@Named("FooImpl1")
	public static class FooImpl1 implements Foo {}
	
	@Component
	public static class FooImpl2 implements Foo {}
	
	private interface Printer {
		
		void print();
		
	}
	
	@Singleton
	private static class PrinterImpl implements Printer {

		@Override
		public void print() {
			System.out.println("print");
		}
		
	}
	
	private static class Bar {
		
		@Inject
		@Named("FooImpl1")
		private Foo foo;
		
		public void test() {
			System.out.println(foo.toString());
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
