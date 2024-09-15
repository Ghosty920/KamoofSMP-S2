package me.ghosty.kamoof.features.drophead;

import me.ghosty.kamoof.KamoofSMP;
import me.ghosty.kamoof.features.ritual.RitualHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import xyz.haoshoku.nick.api.NickAPI;

public final class HeadDropper implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String pacte = RitualHandler.getPacte(player);
		if (pacte != null) {
			if (pacte.equalsIgnoreCase("2"))
				return;
			if (pacte.equalsIgnoreCase("1")) {
				ItemStack skull = SkullManager.getSkull(NickAPI.getOriginalName(player));
				skull.setAmount(KamoofSMP.config().getInt("ritual.pactes.bloody.heads"));
				event.getDrops().add(skull);
				return;
			}
		}
			if (KamoofSMP.config().getBoolean("drophead.enabled")) {
				event.getDrops().add(SkullManager.getSkull(NickAPI.getOriginalName(player)));
			}
	}
	
}
