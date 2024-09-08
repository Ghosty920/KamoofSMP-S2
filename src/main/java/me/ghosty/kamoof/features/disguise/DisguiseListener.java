package me.ghosty.kamoof.features.disguise;

import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;

public class DisguiseListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		if(event.getJoinMessage() != null)
			event.setJoinMessage(event.getJoinMessage().replaceFirst("\\(formerly known as [A-Za-z0-9_]{1,16}\\) ", ""));
	}
	
}
