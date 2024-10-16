package cc.ghosty.kamoof.utils;

import com.google.common.base.Joiner;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

@UtilityClass
public final class Utils {
	
	public static double interpolate(double start, double end, float fraction) {
		return (start + ((end - start) * fraction));
	}
	
	public static String toString(ConfigurationSerializable serializable) {
		return Joiner.on(",").withKeyValueSeparator("=").join(serializable.serialize());
	}
	
}
