package cc.ghosty.kamoof.features.other;

import cc.ghosty.kamoof.features.ritual.RitualHandler;
import cc.ghosty.kamoof.utils.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static cc.ghosty.kamoof.KamoofSMP.*;

public final class JoinMessages implements Listener {
	
	public final boolean ritual = config().getBoolean("ritual.enabled");
	public boolean done = data().getBoolean("firstJoinMsg", false);
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("kamoofsmp.admin") && !player.isOp())
			return;
		
		if (!done) {
			
			Lang.send(player, "FIRST_JOIN");
			
			done = true;
			data().set("firstJoinMsg", true);
			saveData();
			
		} else if (ritual && !RitualHandler.setup) {
			
			Lang.send(player, "RITUAL_NOT_PLACED");
			
		}
	}
	
}
