package im.ghosty.kamoof.utils;

import lombok.experimental.UtilityClass;

import java.util.*;

/**
 * Classe utilitaire pour remplacer des placeholders dans un message.
 */
@UtilityClass
public final class Placeholder {
	
	public static String apply(String string, Map<String, Object> placeholders) {
		if (placeholders == null || placeholders.isEmpty())
			return string;
		String result = string;
		final String[] keys = placeholders.keySet().toArray(new String[0]);
		final Object[] values = placeholders.values().toArray();
		for (int i = 0; i < placeholders.size(); i++)
			result = result.replace("%" + keys[i] + "%", values[i].toString());
		return result;
	}
	
	public static String[] apply(String[] strings, Map<String, Object> placeholders) {
		if (placeholders == null || placeholders.isEmpty())
			return strings;
		String[] results = new String[strings.length];
		final String[] keys = placeholders.keySet().toArray(new String[0]);
		final Object[] values = placeholders.values().toArray();
		for (int i = 0; i < strings.length; i++) {
			String result = strings[i];
			for (int j = 0; j < placeholders.size(); j++)
				result = result.replace("%" + keys[j] + "%", values[j].toString());
			results[i] = result;
		}
		return results;
	}
	
	public static ArrayList<String> apply(List<String> strings, Map<String, Object> placeholders) {
		if (placeholders == null || placeholders.isEmpty())
			return new ArrayList<>(strings);
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
