package cc.ghosty.kamoof.features.other;

import cc.ghosty.kamoof.features.Feature;
import cc.ghosty.kamoof.features.ritual.RitualHandler;
import cc.ghosty.kamoof.utils.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import static cc.ghosty.kamoof.KamoofPlugin.*;

/**
 * {@link Feature} pour rappeller au joueur de placer le rituel, et guide de départ
 * @since 1.0
 */
public final class JoinMessages extends Feature {
	
	public final boolean ritual = config().getBoolean("ritual.enabled");
	public boolean done = data().getBoolean("firstJoinMsg", false);
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
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
