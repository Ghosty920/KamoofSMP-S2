package cc.ghosty.kamoof.features.disguise;

import cc.ghosty.kamoof.KamoofPlugin;
import cc.ghosty.kamoof.api.KamoofSMP;
import cc.ghosty.kamoof.utils.Reflection;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

/**
 * Classe utilitaire pour déguiser un joueur avec n'importe quel pseudo à l'aide de {@link NickAPI}.
 * @since 1.0
 */
@UtilityClass
public final class DisguiseManager {
	
	/**
	 * Déguiser le {@link Player} donné avec le pseudo <code>name</code>.
	 * @param player La cible
	 * @param name Le déguisement
	 */
	public static void disguise(Player player, String name) {
		player.setDisplayName(player.getDisplayName().replace(player.getName(), name));
		
		setNick(player, name);
		NickAPI.setSkin(player, name);
		NickAPI.setUniqueId(player, name);
		setGameProfileName(player, name);
		NickAPI.refreshPlayer(player);
	}
	
	/**
	 * Retirer le déguisement du {@link Player} donné.
	 * @param player La cible
	 */
	public static void undisguise(Player player) {
		player.setDisplayName(player.getDisplayName().replace(NickAPI.getName(player), KamoofSMP.getInstance().getName(player)));
		
		NickAPI.resetNick(player);
		NickAPI.resetSkin(player);
		NickAPI.resetUniqueId(player);
		resetGameProfileName(player);
		NickAPI.refreshPlayer(player);
	}
	
	/**
	 * Retirer le GameProfile du {@link Player}.
	 * <p>
	 * Cette fonction existe pour une compatibilité entre NickAPI v6 et v7.
	 * @param player La cible
	 */
	private static void resetGameProfileName(Player player) {
		try {
			NickAPI.resetGameProfileName(player);
		} catch (Throwable exc) {
			Reflection.getMethod(NickAPI.class, "resetProfileName", Player.class).invoke(null, player);
		}
	}
	
	/**
	 * Retirer le GameProfile du {@link Player} donné avec le pseudo <code>name</code>.
	 * <p>
	 * Cette fonction existe pour une compatibilité entre NickAPI v6 et v7.
	 * @param player La cible
	 * @param name Le déguisement
	 */
	private static void setGameProfileName(Player player, String name) {
		try {
			NickAPI.setGameProfileName(player, name);
		} catch (Throwable exc) {
			Reflection.getMethod(NickAPI.class, "setProfileName", Player.class, String.class).invoke(null, player, name);
		}
	}
	
	/**
	 * Changer le pseudo du {@link Player} par <code>name</code>.
	 * <p>
	 * Cette fonction existe pour une compatibilité entre NickAPI v6 et v7.
	 * @param player La cible
	 * @param name Le déguisement
	 */
	private static void setNick(Player player, String name) {
		try {
			NickAPI.nick(player, name);
		} catch (Throwable exc) {
			Reflection.getMethod(NickAPI.class, "setNick", Player.class, String.class).invoke(null, player, name);
		}
	}
	
}

