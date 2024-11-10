package cc.ghosty.kamoof.utils;

import cc.ghosty.kamoof.KamoofPlugin;
import lombok.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Classe responsable de récupérer les traductions du plugin depuis le fichier adapté (du dossier <code>plugins</code>).
 */
@RequiredArgsConstructor
public final class Lang {
	
	public static final String PREFIX = "<green><b>[KamoofSMP] <reset>";
	public static final String SUPPORT = "https://discord.gg/akgp49Q76M";
	public static final HashMap<String, String> languages = new HashMap<>();
	private static final HashMap<String, String> messages = new HashMap<>();
	@Getter
	private static String locale;
	
	static {
		languages.put("fr", "Français");
		languages.put("en", "English");
	}
	
	@SneakyThrows
	public static void init() {
		locale = "fr";
		switch (KamoofPlugin.config().getString("language").toLowerCase().trim()) {
			case "en":
			case "en_uk":
			case "en_en":
			case "en_us":
			case "england":
			case "uk":
			case "us":
			case "english":
			case "usa":
			case "anglais": {
				locale = "en";
				break;
			}
		}
		
		String resourceName = "messages" + File.separator + "messages_" + locale + ".yml";
		InputStreamReader reader = new InputStreamReader(Lang.class.getClassLoader().getResourceAsStream(resourceName));
		YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
		messages.clear();
		config.getValues(true).forEach((key, value) -> messages.put(key, value.toString()));
	}
	
	public static String get(String key) {
		return messages.getOrDefault(key, "UNKNOWN Locale=" + locale + " Key=" + key).replace("%PREFIX%", PREFIX);
	}
	
	public static void send(CommandSender sender, String key, Object... args) {
		String value = get(key);
		if (args == null || args.length == 0)
			sender.spigot().sendMessage(Message.toBaseComponent(value));
		else
			sender.spigot().sendMessage(Message.toBaseComponent(String.format(value, args)));
	}
	
}
