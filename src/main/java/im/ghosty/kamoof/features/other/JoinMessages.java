package im.ghosty.kamoof.features.other;

import im.ghosty.kamoof.features.Feature;
import im.ghosty.kamoof.features.ritual.RitualHandler;
import im.ghosty.kamoof.utils.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static im.ghosty.kamoof.KamoofPlugin.*;

/**
 * {@link Feature} pour rappeller au joueur de placer le rituel, et guide de départ
 * @since 1.0
 */
public final class JoinMessages extends Feature {
	
	public final boolean ritual = CompatibilityUtils.isMinecraft1_21() && config().getBoolean("ritual.enabled");
	public static boolean done = data().getBoolean("firstJoinMsg", false);
	public static boolean ignoreSpigot = data().getBoolean("ignoreSpigotCrash", false);
	
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
		
		if(!ignoreSpigot && !CompatibilityUtils.IS_RUNNING_PAPER) {
			Lang.send(player, "SPIGOT_FOUND");
		}
		
		nickApi: {
			File file = Utils.findNickAPIFile();
			if(file == null) break nickApi;
			Lang.send(player, "NICKAPI_FOUND");
			Lang.send(player, "NICKAPI_FOUND_" + (file.getPath().contains(".paper-remapped") ? "PAPER" : "SPIGOT"), file.getName());
		}
	}
	
}
