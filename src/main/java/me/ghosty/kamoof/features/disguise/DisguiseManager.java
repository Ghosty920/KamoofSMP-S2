package me.ghosty.kamoof.features.disguise;

import lombok.experimental.UtilityClass;
import me.ghosty.kamoof.KamoofSMP;
import me.ghosty.kamoof.utils.Reflection;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

@UtilityClass
public final class DisguiseManager {
	
	public static void disguise(Player player, String name) {
		player.setDisplayName(player.getDisplayName().replace(player.getName(), name));
		
		NickAPI.nick(player, name);
		NickAPI.setSkin(player, name);
		NickAPI.setUniqueId(player, name);
//		if (KamoofSMP.config().getBoolean("options.gameprofile"))
			NickAPI.setGameProfileName(player, name);
		NickAPI.refreshPlayer(player);
	}
	
	public static void undisguise(Player player) {
		player.setDisplayName(player.getDisplayName().replace(NickAPI.getName(player), NickAPI.getOriginalName(player)));
		
		NickAPI.resetNick(player);
		NickAPI.resetSkin(player);
		NickAPI.resetUniqueId(player);
//		if (KamoofSMP.config().getBoolean("options.gameprofile"))
			NickAPI.resetGameProfileName(player);
		NickAPI.refreshPlayer(player);
	}
	
	private static void resetGameProfileName(Player player) {
		try {
			Reflection.getMethod(NickAPI.class, "resetProfileName", Player.class).invoke(null, player);
		} catch(Exception exc) {
			NickAPI.resetGameProfileName(player);
		}
	}
	
	private static void setGameProfileName(Player player, String name) {
		try {
			Reflection.getMethod(NickAPI.class, "setProfileName", Player.class, String.class).invoke(null, player, name);
		} catch(Exception exc) {
			NickAPI.setGameProfileName(player, name);
		}
	}
	
	private static void setNick(Player player, String name) {
		try {
			Reflection.getMethod(NickAPI.class, "setNick", Player.class, String.class).invoke(null, player, name);
		} catch(Exception exc) {
			NickAPI.nick(player, name);
		}
	}
	
}

