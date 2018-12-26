package io.vilya.maia.ip.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.netty.util.internal.StringUtil;
import io.vilya.maia.ip.constant.MediaType;

/**
 * 
 * @author vilya
 *
 */
public class Accept implements Comparable<Accept> {

	public static final Accept EMPTY = new Accept();

	public static final Pattern PATTERN = Pattern
			.compile("^(?<type>[A-Za-z*]+)/(?<subType>[A-Za-z+*]+)(;level=(?<level>[0-9]+))?(;q=(?<q>[0-9.]+))?.*$");

	private static final String WILD_CARD = "*";

	private String type;

	private String subType;

	private String q;

	public static Accept of(String raw) {
		Matcher matcher = PATTERN.matcher(raw.trim());
		if (matcher.matches()) {
			Accept accept = new Accept();
			accept.setType(matcher.group("type"));
			accept.setSubType(matcher.group("subType"));
			accept.setQ(matcher.group("q"));
			return accept;
		} else {
			return EMPTY;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public boolean isCompatibleWith(String contentType) {
		if (StringUtil.isNullOrEmpty(contentType)) {
			return false;
		}

		int index = contentType.indexOf(';');
		if (index != -1) {
			contentType = contentType.substring(0, index);
		}

		String[] types = contentType.split("/");
		if (types.length != 2) {
			return false;
		}

		if (WILD_CARD.equals(type)) {
			return true;
		} else {
			return types[0].equals(type) && (WILD_CARD.equals(subType) || types[1].equals(subType));
		}
	}

	public boolean isCompatibleWith(MediaType mediaType) {
		if (mediaType == null) {
			return false;
		}

		if (WILD_CARD.equals(type)) {
			return true;
		} else {
			return mediaType.getType().equals(type)
					&& (WILD_CARD.equals(subType) || mediaType.getSubType().equals(subType));
		}
	}

	@Override
	public String toString() {
		return "Accept [type=" + type + ", subType=" + subType + ", q=" + q + "]";
	}

	@Override
	public int compareTo(Accept o) {
		if (o == null) {
			return -1;
		}

		if (q == null && o.getQ() == null) {
			return 0;
		}

		if (q == null || o.getQ() == null) {
			return q == null ? -1 : 1;
		}

		return -q.compareTo(o.getQ()); // desc
	}

}
