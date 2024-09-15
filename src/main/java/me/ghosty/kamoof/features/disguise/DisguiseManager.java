package me.ghosty.kamoof.features.disguise;

import lombok.experimental.UtilityClass;
import me.ghosty.kamoof.utils.Reflection;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

@UtilityClass
public final class DisguiseManager {
	
	public static void disguise(Player player, String name) {
		player.setDisplayName(player.getDisplayName().replace(player.getName(), name));
		
		setNick(player, name);
		NickAPI.setSkin(player, name);
		NickAPI.setUniqueId(player, name);
		setGameProfileName(player, name);
		NickAPI.refreshPlayer(player);
	}
	
	public static void undisguise(Player player) {
		player.setDisplayName(player.getDisplayName().replace(NickAPI.getName(player), NickAPI.getOriginalName(player)));
		
		NickAPI.resetNick(player);
		NickAPI.resetSkin(player);
		NickAPI.resetUniqueId(player);
		resetGameProfileName(player);
		NickAPI.refreshPlayer(player);
	}
	
	private static void resetGameProfileName(Player player) {
		try {
			NickAPI.resetGameProfileName(player);
		} catch (Throwable exc) {
			Reflection.getMethod(NickAPI.class, "resetProfileName", Player.class).invoke(null, player);
		}
	}
	
	private static void setGameProfileName(Player player, String name) {
		try {
			NickAPI.setGameProfileName(player, name);
		} catch (Throwable exc) {
			Reflection.getMethod(NickAPI.class, "setProfileName", Player.class, String.class).invoke(null, player, name);
		}
	}
	
	private static void setNick(Player player, String name) {
		try {
			NickAPI.nick(player, name);
		} catch (Throwable exc) {
			Reflection.getMethod(NickAPI.class, "setNick", Player.class, String.class).invoke(null, player, name);
		}
	}
	
}

