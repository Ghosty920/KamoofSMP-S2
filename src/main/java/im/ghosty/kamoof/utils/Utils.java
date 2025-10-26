package im.ghosty.kamoof.utils;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import im.ghosty.kamoof.KamoofPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.net.URL;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
	
	public static void log(String msg, Object... args) {
		Bukkit.getLogger().fine(String.format(msg, args));
	}
	
	public static File findNickAPIFile() {
		try {
			Class<?> clazz = Bukkit.getPluginManager().getPlugin("NickAPI").getClass();
			//Class<?> clazz = Class.forName("xyz.haoshoku.nick.NickPlugin"); // fsr ne marche pas???
			URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
			return new File(url.toURI());
		} catch (Throwable exc) {
			return null;
		}
	}
	
	/**
	 * @param url L'url du fichier dans le dossier paper remapped
	 * @return Le fichier original
	 */
	public static File getParentPluginFile(URL url) {
		try {
			if (url.getPath().contains(".paper-remapped")) {
				String[] split = url.getPath().split("/");
				url = new File(new File(url.toURI()).getParentFile().getParent(), split[split.length - 1]).toURI().toURL();
			}
			return new File(url.toURI());
		} catch (Throwable exc) {
			exc.printStackTrace();
			return null;
		}
	}
	
	public JsonElement getFirstMatchArray(JsonArray array, Predicate<JsonElement> predicate) {
		for (JsonElement element : array) {
			try {
				if (predicate.test(element))
					return element;
			} catch (Throwable ignored) {
			}
		}
		return null;
	}
	
}
