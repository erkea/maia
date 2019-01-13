/**
 * 
 */
package io.vilya.maia.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
public class GenericTest {

	@Test
	public void test1() {
		System.out.println(Arrays.toString(AbstractTypeA.class.getTypeParameters()));
	}
	
	@Test
	public void test2() {
		TypeA<Integer> a = new TypeA<Integer>() {};		
		Arrays.stream(a.getClass().getGenericInterfaces()).forEach(t -> {
			System.out.println(Arrays.toString(((ParameterizedType) t).getActualTypeArguments()));
		});
	}
	
	@Test
	public void test3() {
		AbstractTypeA<Integer> a = new AbstractTypeA<Integer>() {};
		Type t = a.getClass().getGenericSuperclass();
		System.out.println(Arrays.toString(((ParameterizedType) t).getActualTypeArguments()));
	}
	
	private interface TypeA<T> {
		
	}
	
	private static class AbstractTypeA<T> {
		
	}
	
	@Test
	public void test4() {
		List<Typei> types = new ArrayList<>();
		types.add(new Type1());
		types.add(new Type2());
	}
	
	@Test
	public void test5() {
		List<? extends Type> types = new ArrayList<>();
		// types.add(new Type1());
		// types.add(new Type1());
	}
	
	private interface Typei {}
	
	private static class Type1 implements Typei {}
	
	private static class Type2 implements Typei {}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
