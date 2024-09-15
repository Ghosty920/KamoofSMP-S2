package me.ghosty.kamoof.features.disguise;

import me.ghosty.kamoof.KamoofSMP;
import me.ghosty.kamoof.utils.Message;
import me.ghosty.kamoof.utils.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.Map;

public final class DisguiseRestaurer implements Listener {
	
	private static boolean enabled = false;
	
	public DisguiseRestaurer() {
		enabled = true;
	}
	
	public static String get(String name) {
		return KamoofSMP.getData().getString("restaurer." + name);
	}
	
	public static void set(String name, String disguise) {
		if (name.equalsIgnoreCase(disguise))
			KamoofSMP.getData().set("restaurer." + name, null);
		else
			KamoofSMP.getData().set("restaurer." + name, disguise);
		KamoofSMP.saveData();
	}
	
	public static void onEnable() {
		if (!enabled)
			return;
		
		Bukkit.getOnlinePlayers().forEach(player -> {
			String name = player.getName();
			String disguise = get(name);
			if (disguise != null) {
				set(name, null);
				DisguiseManager.disguise(player, disguise);
			}
		});
		
		KamoofSMP.saveData();
	}
	
	public static void onDisable() {
		if (!enabled)
			return;
		
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (NickAPI.isNicked(player))
				set(NickAPI.getOriginalName(player), NickAPI.getName(player));
		});
		
		KamoofSMP.saveData();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		String disguise = get(name);
		if (disguise == null)
			return;
		set(name, null);
		DisguiseManager.disguise(player, disguise);
		
		labelJoinMessage:
		{
			String message = event.getJoinMessage();
			if (message == null)
				break labelJoinMessage;
			event.setJoinMessage(message.replace(name, disguise));
		}
		
		String message = KamoofSMP.config().getString("restaure.message");
		if (message.isBlank())
			return;
		player.spigot().sendMessage(Message.toBaseComponent(Placeholder.apply(message, Map.of("player", name, "nick", disguise))));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		try {
			if (!NickAPI.isNicked(player))
				return;
			set(NickAPI.getOriginalName(player), NickAPI.getName(player));
		} catch (Throwable ignored) {
		}
	}
}
