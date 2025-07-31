package im.ghosty.kamoof.features.disguise;

import im.ghosty.kamoof.api.KamoofSMP;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.NickAPI;

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
		
		NickAPI.nick(player, name);
		NickAPI.setSkin(player, name);
		NickAPI.setUniqueId(player, name);
		NickAPI.setGameProfileName(player, name);
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
		NickAPI.resetGameProfileName(player);
		NickAPI.refreshPlayer(player);
	}
	
}

