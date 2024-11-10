package cc.ghosty.kamoof.features.disguise;

import cc.ghosty.kamoof.KamoofPlugin;
import cc.ghosty.kamoof.features.Feature;
import cc.ghosty.kamoof.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static cc.ghosty.kamoof.KamoofPlugin.*;

/**
 * La Feature
 * @since 1.0
 */
public final class DisguiseRestaurer extends Feature {
	
	@Override
	public boolean isEnabled() {
		return config().getBoolean("disguise.restaure");
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	public static String get(UUID uuid) {
		return data().getString("restaurer." + uuid);
	}
	
	public static void set(UUID uuid, String disguise) {
		data().set("restaurer." + uuid, disguise);
		saveData();
	}
	
	public static HashMap<OfflinePlayer, String> getRestaures() {
		HashMap<OfflinePlayer, String> map = new HashMap<>();
		AtomicBoolean shouldSaveData = new AtomicBoolean(false);
		
		data().getConfigurationSection("restaurer").getValues(false).forEach((uuid, disguise) -> {
			try {
				OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
				if(player.getName() == null)
					throw new RuntimeException();
				map.put(player, (String) disguise);
			} catch(Throwable exc) {
				data().set("restaurer."+uuid, null);
				shouldSaveData.set(true);
			}
		});
		
		if(shouldSaveData.get())
			saveData();
		return map;
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		Bukkit.getScheduler().runTaskLater(KamoofPlugin.getInstance(), () -> {
			Bukkit.getOnlinePlayers().forEach(player -> {
				String name = player.getName();
				String disguise = get(player.getUniqueId());
				if (disguise != null) {
					set(player.getUniqueId(), null);
					if (!name.equalsIgnoreCase(disguise))
						KamoofPlugin.getInstance().disguise(player, disguise);
				}
			});
			saveData();
		}, 1L);
		
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if(!enabled) {
			data().set("restaurer", null);
			saveData();
			return;
		}
		Bukkit.getOnlinePlayers().forEach(player -> {
			Bukkit.broadcastMessage(player.getName());
			if (NickAPI.isNicked(player) && !NickAPI.getOriginalName(player).equalsIgnoreCase(NickAPI.getName(player)))
				set(player.getUniqueId(), NickAPI.getName(player));
		});
		saveData();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
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
			System.out.println(event.getJoinMessage());
			if (message == null)
				break labelJoinMessage;
			event.setJoinMessage(message.replace(name, disguise));
		}
		
		Message.send(player, "messages.restaure", Map.of("player", name, "nick", disguise));
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
