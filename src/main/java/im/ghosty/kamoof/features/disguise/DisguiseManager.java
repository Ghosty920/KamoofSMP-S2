package im.ghosty.kamoof.features.disguise;

import im.ghosty.kamoof.api.KamoofSMP;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.NickAPI;

import java.util.Objects;

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
		// nom invalide, on part de l'idée que c'est un undisguise (on déguise en rien)
		if(name == null || name.isEmpty()) {
			undisguise(player);
			return;
		}
		// B (A) -> B, inutile
		if(Objects.equals(KamoofSMP.getInstance().getDisguise(player), name))
			return;
		// B (A) -> A, il utilise sa propre tête, autant retirer son déguisement
		if(Objects.equals(player.getName(), name)) {
			undisguise(player);
			return;
		}
		
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
		// s'il n'est pas déguisé, évitons de refresh dans le vide
		if(!NickAPI.isNicked(player)) return;
		
		player.setDisplayName(player.getDisplayName().replace(NickAPI.getName(player), KamoofSMP.getInstance().getName(player)));
		
		NickAPI.resetNick(player);
		NickAPI.resetSkin(player);
		NickAPI.resetUniqueId(player);
		NickAPI.resetGameProfileName(player);
		NickAPI.refreshPlayer(player);
	}
	
}

