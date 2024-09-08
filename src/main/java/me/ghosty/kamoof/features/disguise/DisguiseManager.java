package me.ghosty.kamoof.features.disguise;

import lombok.experimental.UtilityClass;
import me.ghosty.kamoof.KamoofSMP;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

@UtilityClass
public final class DisguiseManager {
	
	public static void disguise(Player player, String name) {
		player.setDisplayName(player.getDisplayName().replace(player.getName(), name));
		
		NickAPI.setNick(player, name);
		NickAPI.setSkin(player, name);
		NickAPI.setUniqueId(player, name);
		if (KamoofSMP.config().getBoolean("options.gameprofile"))
			NickAPI.setProfileName(player, name);
		NickAPI.refreshPlayer(player);
	}
	
	public static void undisguise(Player player) {
		player.setDisplayName(player.getDisplayName().replace(NickAPI.getName(player), NickAPI.getOriginalName(player)));
		
		NickAPI.resetNick(player);
		NickAPI.resetSkin(player);
		NickAPI.resetUniqueId(player);
		if (KamoofSMP.config().getBoolean("options.gameprofile"))
			NickAPI.resetProfileName(player);
		NickAPI.refreshPlayer(player);
	}
	
}

