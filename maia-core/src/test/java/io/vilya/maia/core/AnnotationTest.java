/**
 * 
 */
package io.vilya.maia.core;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.vilya.maia.core.annotation.Component;
import io.vilya.maia.core.annotation.Controller;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
public class AnnotationTest {

	@Test
	public void test1() {
		System.out.println(Type1.class.isAnnotationPresent(Component.class));
	}
	
	@Test
	public void test2() {
		System.out.println(Type2.class.isAnnotationPresent(Component.class));
	}
	
	@Test
	public void test3() {
		System.out.println(Arrays.toString(Type3.class.getAnnotations()));
	}
	
	@Test
	public void test4() {
		Arrays.stream(Type3.class.getAnnotations()).forEach(t -> {
			System.out.println(t);
			System.out.println(t.annotationType().isAnnotationPresent(Component.class));
		});
	}
	
	@Component
	private static abstract class AbstractType {
		
	}
	
	private static class Type1 extends AbstractType {
		
	}
	
	@Component
	private static class Type2 {
		
	}
	
	@Controller
	private static class Type3 {
		
	}
	
}
