package io.vilya.maia.ip.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.util.ArrayIterator;

import io.netty.util.internal.StringUtil;

/**
 * 
 * @author vilya
 *
 */
public class Accepts implements Iterable<Accept> {

	private List<Accept> accepts;
	
	public static List<Accept> of(String raw) {
		if (StringUtil.isNullOrEmpty(raw)) {
			return Collections.emptyList();
		}

		String[] rawAccepts = raw.split(",");
		return Arrays.stream(rawAccepts).map(Accept::of).collect(Collectors.toList());
	}

	public static List<Accept> ofOrdered(String raw) {
		if (StringUtil.isNullOrEmpty(raw)) {
			return Collections.emptyList();
		}

		String[] rawAccepts = raw.split(",");
		return Arrays.stream(rawAccepts).map(Accept::of).sorted().collect(Collectors.toList());
	}
	
	@Override
	public Iterator<Accept> iterator() {
		return new ArrayIterator<>(accepts.toArray(new Accept[0]));
	}

}
