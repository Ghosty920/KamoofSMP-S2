package cc.ghosty.kamoof.features.disguise;

import cc.ghosty.kamoof.KamoofSMP;
import cc.ghosty.kamoof.utils.Message;
import cc.ghosty.kamoof.utils.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.Map;
import java.util.UUID;

import static cc.ghosty.kamoof.KamoofSMP.*;

public final class DisguiseRestaurer implements Listener {
	
	private static boolean enabled = false;
	
	public DisguiseRestaurer() {
		enabled = true;
	}
	
	public static String get(UUID uuid) {
		return data().getString("restaurer." + uuid);
	}
	
	public static void set(UUID uuid, String disguise) {
		data().set("restaurer." + uuid, disguise);
		saveData();
	}
	
	public static void onEnable() {
		if (!enabled)
			return;
		
		Bukkit.getScheduler().runTaskLater(KamoofSMP.getInstance(), () -> {
			Bukkit.getOnlinePlayers().forEach(player -> {
				String name = player.getName();
				String disguise = get(player.getUniqueId());
				if (disguise != null) {
					set(player.getUniqueId(), null);
					if (!name.equalsIgnoreCase(disguise))
						DisguiseManager.disguise(player, disguise);
				}
			});
			saveData();
		}, 1L);
		
	}
	
	public static void onDisable() {
		if (!enabled)
			return;
		
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (NickAPI.isNicked(player) && !NickAPI.getOriginalName(player).equalsIgnoreCase(NickAPI.getName(player)))
				set(player.getUniqueId(), NickAPI.getName(player));
		});
		
		saveData();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		UUID uuid = player.getUniqueId();
		String disguise = get(uuid);
		if (disguise == null)
			return;
		set(uuid, null);
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
			set(player.getUniqueId(), NickAPI.getName(player));
		} catch (Throwable ignored) {
		}
	}
}
