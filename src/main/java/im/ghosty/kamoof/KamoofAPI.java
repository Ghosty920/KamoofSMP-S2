package im.ghosty.kamoof;

import im.ghosty.kamoof.api.KamoofSMP;
import im.ghosty.kamoof.features.disguise.DisguiseManager;
import im.ghosty.kamoof.features.disguise.DisguiseRestaurer;
import im.ghosty.kamoof.features.drophead.SkullManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.haoshoku.nick.NickAPI;

/**
 * L'instanciation de l'API du KamoofSMP.
 */
public final class KamoofAPI extends KamoofSMP {
	
	KamoofAPI() {
		super();
	}
	
	@Override
	public ItemStack getHead(OfflinePlayer player) {
		return SkullManager.getSkull(getName(player));
	}
	
	@Override
	public void disguise(OfflinePlayer player, String name) {
		if(player instanceof Player p) {
			if(name != null)
				DisguiseManager.disguise(p, name);
			else
				DisguiseManager.undisguise(p);
		} else {
			DisguiseRestaurer.set(player.getUniqueId(), name);
		}
	}
	
	@Override
	public String getDisguise(OfflinePlayer player) {
		if(player instanceof Player p) {
			if(!NickAPI.isNicked(p))
				return null;
			return NickAPI.getName(p);
		} else {
			return DisguiseRestaurer.get(player.getUniqueId());
		}
	}
	
	@Override
	public String getName(OfflinePlayer player) {
		return player instanceof Player p ? NickAPI.getOriginalName(p) : player.getName();
	}
	
}
