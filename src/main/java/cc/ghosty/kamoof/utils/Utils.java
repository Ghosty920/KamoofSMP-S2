package cc.ghosty.kamoof.utils;

import com.google.common.base.Joiner;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Classe utilitaire en tout genre..
 */
@UtilityClass
public final class Utils {
	
	public static double interpolate(double start, double end, float fraction) {
		return (start + ((end - start) * fraction));
	}
	
	public static String toString(ConfigurationSerializable serializable) {
		return Joiner.on(",").withKeyValueSeparator("=").join(serializable.serialize());
	}
	
}
