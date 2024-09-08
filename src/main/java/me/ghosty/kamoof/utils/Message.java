package me.ghosty.kamoof.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Based on <a href="https://github.com/Luncaaa/AdvancedDisplays/blob/main/api/src/main/java/me/lucaaa/advanceddisplays/api/util/ComponentSerializer.java">AdvancedDisplays</a> under GPL-3.0
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
	 * Transforms a list of strings into a component. Each element in the list will be considered a new line.
	 * Supports MiniMessage format and legacy color codes.
	 *
	 * @param text The list of strings to convert into a component.
	 * @return The component.
	 */
	public static Component deserialize(List<String> text) {
		return deserialize(String.join("\n", text));
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
	
	/**
	 * Transforms a component into a list of strings with MiniMessage format. Every "\n" will be considered as a new line.
	 *
	 * @param component The component to convert into a list of strings.
	 * @return The list of strings.
	 */
	public static List<String> serialize(Component component) {
		return Arrays.stream(MiniMessage.miniMessage().serialize(component).split(Pattern.quote("\n"))).toList();
	}
	
	/**
	 * Transforms a component into a JSON string.
	 *
	 * @param component The component to convert into a JSON string.
	 * @return The JSON string.
	 */
	public static String toJSON(Component component) {
		return net.md_5.bungee.chat.ComponentSerializer.toString(BungeeComponentSerializer.get().serialize(component));
	}
	
	
}
