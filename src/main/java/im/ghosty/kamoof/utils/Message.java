package im.ghosty.kamoof.utils;

import im.ghosty.kamoof.KamoofPlugin;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Classe utilitaire pour gérer {@link MiniMessage}
 * <p>
 * Basé sur <a href="https://github.com/Luncaaa/AdvancedDisplays/blob/main/api/src/main/java/me/lucaaa/advanceddisplays/api/util/ComponentSerializer.java">AdvancedDisplays</a> sous GPL-3.0
 */
@UtilityClass
public final class Message {
	
	/**
	 * Transforms a string into a component. Every "\n" will be considered as a new line.
	 * Supports MiniMessage format and legacy color codes.
	 *
	 * @param text The string to convert into a component.
	 * @return The component.
	 */
	public static Component deserialize(String text) {
		text = text.replace("\\n", "\n");
		Component legacy = LegacyComponentSerializer.legacyAmpersand().deserialize(text);
		// Replacing the "\" with nothing makes the MiniMessage formats work.
		String minimessage = MiniMessage.miniMessage().serialize(legacy).replace("\\", "");
		return MiniMessage.miniMessage().deserialize(minimessage);
	}
	
	/**
	 * Transforms a string into a Bungee component. Every "\n" will be considered as a new line.
	 * Supports Minimessage format and legacy color codes.
	 *
	 * @param text The string to convert into a component.
	 * @return The component.
	 */
	public static BaseComponent[] toBaseComponent(String text) {
		return BungeeComponentSerializer.get().serialize(deserialize(text));
	}
	
	public static void send(CommandSender sender, String path, Map<String, Object> placeholders) {
		if(sender == null)
			return;
		if(sender instanceof OfflinePlayer player && !player.isOnline())
			return;
		String msg = KamoofPlugin.config().getString(path);
		if(msg.isBlank())
			return;
		sender.spigot().sendMessage(toBaseComponent(Placeholder.apply(msg, placeholders)));
	}
	
	public static void send(CommandSender sender, String message) {
		if(sender == null)
			return;
		if(sender instanceof OfflinePlayer player && !player.isOnline())
			return;
		if(message == null || message.isBlank())
			return;
		sender.spigot().sendMessage(toBaseComponent(message));
	}
	
}
