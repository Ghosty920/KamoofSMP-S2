package im.ghosty.kamoof.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Color;

import java.lang.reflect.Field;
import java.util.HashMap;

import static java.lang.Integer.toHexString;

/**
 * Classe utilitaire pour récupérer une couleur à partir de son nom ou de son code hexadécimal.
 */
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
		
		try {
			return toBukkit(getJavaColor(color));
		} catch (Throwable exc) {
			return null;
		}
	}
	
	public static java.awt.Color getJavaColor(String color) {
		return java.awt.Color.decode(color);
	}
	
	public static Color toBukkit(java.awt.Color color) {
		if (color == null)
			return null;
		return Color.fromARGB(color.getRGB());
	}
	
	public static String toLegacy(Color color) {
		if (color == null)
			return "";
		int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();
		return "§x§" + toHexString(red >> 4) + "§" + toHexString(red & 0x0F)
			+ "§" + toHexString(green >> 4) + "§" + toHexString(green & 0x0F)
			+ "§" + toHexString(blue >> 4) + "§" + toHexString(blue & 0x0F);
	}
	
}
