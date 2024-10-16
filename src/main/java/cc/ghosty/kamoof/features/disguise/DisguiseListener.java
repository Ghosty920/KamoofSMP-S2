package cc.ghosty.kamoof.features.disguise;

import cc.ghosty.kamoof.KamoofSMP;
import cc.ghosty.kamoof.features.drophead.SkullManager;
import cc.ghosty.kamoof.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.Map;

public final class DisguiseListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		if (event.getJoinMessage() != null)
			event.setJoinMessage(event.getJoinMessage().replaceFirst("\\(formerly known as [A-Za-z0-9_]{1,16}\\) ", ""));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		OfflinePlayer target = SkullManager.getOwner(event.getItem());
		if (target == null)
			return;
		
		switch (event.getAction()) {
			case LEFT_CLICK_BLOCK, LEFT_CLICK_AIR, PHYSICAL -> {
				return;
			}
			case RIGHT_CLICK_BLOCK -> {
				if (!KamoofSMP.config().getBoolean("disguise.place-head"))
					event.setCancelled(true);
				return;
			}
		}
		
		String name = "";
		switch (NickVersion.get()) {
			case v6 -> {
				name = target.getName();
				if (target instanceof Player targetP)
					name = NickAPI.getOriginalName(targetP);
				if (name == null)
					name = SkullManager.getName(event.getItem());
			}
			case v7 -> {
				name = SkullManager.getName(event.getItem());
				if (name == null) {
					name = target.getName();
					if (target instanceof Player targetP)
						name = NickAPI.getOriginalName(targetP);
				}
			}
		}
		
		if (KamoofSMP.config().getBoolean("disguise.give-back") && NickAPI.isNicked(player)) {
			ItemStack item = SkullManager.getSkull(NickAPI.getName(player));
			if (!player.getInventory().addItem(item).isEmpty())
				player.getWorld().dropItem(player.getLocation(), item);
		}
		DisguiseManager.disguise(player, name);
		Message.send(player, "messages.disguised", Map.of("player", NickAPI.getOriginalName(player), "nick", name));
		
		event.getItem().setAmount(event.getItem().getAmount() - 1);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (!NickAPI.isNicked(player))
			return;
		String disguise = NickAPI.getName(player);
		DisguiseManager.undisguise(player);
		Message.send(player, "messages.lostdisguise", Map.of("player", NickAPI.getOriginalName(player), "nick", disguise));
		
		DisguiseRestaurer.set(player.getUniqueId(), null);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRequestJoin(AsyncPlayerPreLoginEvent event) {
		if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
			return;
		Bukkit.getScheduler().runTask(KamoofSMP.getInstance(), () -> {
			Player player = NickAPI.getPlayerOfNickedName(event.getName());
			if(player == null)
				return;
			NickAPI.setGameProfileName(player, NickAPI.getOriginalGameProfileName(player));
			NickAPI.refreshPlayer(player);
			
			Bukkit.getScheduler().runTaskLater(KamoofSMP.getInstance(), () -> {
				if(!player.isOnline())
					return;
				NickAPI.setGameProfileName(player, event.getName());
				NickAPI.refreshPlayer(player);
			}, 1L);
		});
	}
	
}
