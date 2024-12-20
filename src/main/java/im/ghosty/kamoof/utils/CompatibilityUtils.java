package im.ghosty.kamoof.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.attribute.Attribute;

@UtilityClass
public final class CompatibilityUtils {
	
	private static Attribute MAX_HEALTH_ATTRIBUTE;
	
	public static Attribute getMaxHealthAttribute() {
		if(MAX_HEALTH_ATTRIBUTE != null)
			return MAX_HEALTH_ATTRIBUTE;
		
		try {
			MAX_HEALTH_ATTRIBUTE = Attribute.MAX_HEALTH;
		} catch(Throwable exc) {
			MAX_HEALTH_ATTRIBUTE = Attribute.valueOf("GENERIC_MAX_HEALTH");
		}
		return MAX_HEALTH_ATTRIBUTE;
	}
	
}
