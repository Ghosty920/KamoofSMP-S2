package im.ghosty.kamoof.features.other;

import im.ghosty.kamoof.features.Feature;
import im.ghosty.kamoof.features.ritual.RitualHandler;
import im.ghosty.kamoof.utils.CompatibilityUtils;
import im.ghosty.kamoof.utils.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import static im.ghosty.kamoof.KamoofPlugin.*;

/**
 * {@link Feature} pour rappeller au joueur de placer le rituel, et guide de départ
 * @since 1.0
 */
public final class JoinMessages extends Feature {
	
	public final boolean ritual = CompatibilityUtils.isMinecraft1_21() && config().getBoolean("ritual.enabled");
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
