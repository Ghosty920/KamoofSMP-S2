package me.ghosty.kamoof.features.drophead;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.haoshoku.nick.api.NickAPI;

public final class HeadDropper implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		event.getDrops().add(SkullManager.getSkull(NickAPI.getOriginalName(player)));
	}
	
}
