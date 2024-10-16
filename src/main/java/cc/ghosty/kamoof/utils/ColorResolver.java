package cc.ghosty.kamoof.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Color;

import java.lang.reflect.Field;
import java.util.HashMap;

@UtilityClass
public final class ColorResolver {
	
	private final HashMap<String, Color> colors = new HashMap<>();
	
	static {
		for (Field field : Color.class.getDeclaredFields()) {
			try {
				if (field.getType() != Color.class)
					continue;
				
				if (!field.canAccess(null))
					continue;
				
				colors.put(field.getName().toLowerCase().trim(), (Color) field.get(null));
			} catch (IllegalArgumentException | IllegalAccessException ignored) {
			} catch (Throwable exc) {
				exc.printStackTrace();
			}
		}
	}
	
	public static Color getColor(String color) {
		if (color == null || color.length() < 2)
			return null; // why not
		
		Color stored = colors.get(color.toLowerCase().trim());
		if (stored != null)
			return stored;
		
		return toBukkit(getJavaColor(color));
	}
	
	public static java.awt.Color getJavaColor(String color) {
		return java.awt.Color.decode(color);
	}
	
	public static Color toBukkit(java.awt.Color color) {
		if (color == null)
			return null;
		return Color.fromARGB(color.getRGB());
	}
	
}
