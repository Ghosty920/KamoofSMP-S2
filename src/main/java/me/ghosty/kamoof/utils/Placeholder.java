package me.ghosty.kamoof.utils;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public final class Placeholder {
	
	public static String apply(final String string, final Map<String, Object> placeholders) {
		String result = string;
		final String[] keys = placeholders.keySet().toArray(new String[0]);
		final Object[] values = placeholders.values().toArray();
		for(int i = 0; i < placeholders.size(); i++)
			result = result.replace("%"+keys[i]+"%", values[i].toString());
		return result;
	}
	
	public static String[] apply(String[] strings, Map<String, Object> placeholders) {
		String[] results = new String[strings.length];
		final String[] keys = placeholders.keySet().toArray(new String[0]);
		final Object[] values = placeholders.values().toArray();
		for (int i = 0; i < strings.length; i++) {
			String result = strings[i];
			for(int j = 0; j < placeholders.size(); j++)
				result = result.replace("%"+keys[j]+"%", values[j].toString());
			results[i] = result;
		}
		return results;
	}
	
	public static ArrayList<String> apply(List<String> strings, Map<String, Object> placeholders) {
		ArrayList<String> results = new ArrayList<>(strings.size());
		final String[] keys = placeholders.keySet().toArray(new String[0]);
		final Object[] values = placeholders.values().toArray();
		for (String result : strings) {
			for (int i = 0; i < placeholders.size(); i++)
				result = result.replace("%" + keys[i] + "%", values[i].toString());
			results.add(result);
		}
		return results;
	}
	
}
