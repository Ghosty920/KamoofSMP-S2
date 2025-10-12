package im.ghosty.kamoof.features.drophead;

import im.ghosty.kamoof.KamoofPlugin;
import im.ghosty.kamoof.api.KamoofSMP;
import im.ghosty.kamoof.utils.Placeholder;
import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;

import java.util.Map;

/**
 * Classe utilitaire pour obtenir la tête d'un joueur à partir de son pseudo.
 *
 * @since 1.0
 */
@UtilityClass
public final class SkullManager {
	
	private static final NamespacedKey keyTimestamp = new NamespacedKey("kamoofsmp", "timestamp");
	private static final NamespacedKey keyPlayer = new NamespacedKey("kamoofsmp", "player");
	
	/**
	 * @since 1.0
	 */
	public static ItemStack getSkull(String player) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(player);
		meta.setOwningPlayer(target);
		
		String itemName = Placeholder.apply(KamoofPlugin.config().getString("drophead.name"), Map.of("player", player));
		meta.setDisplayName(itemName);
		try {
			meta.setItemName(itemName);
		} catch (Throwable ignored) {
		}
		meta.setLore(Placeholder.apply(KamoofPlugin.config().getStringList("drophead.lore"), Map.of("player", player)));
		
		boolean stackable = KamoofPlugin.config().getBoolean("drophead.stackable");
		meta.getPersistentDataContainer().set(keyTimestamp, PersistentDataType.LONG, stackable ? -1L : System.currentTimeMillis());
		meta.getPersistentDataContainer().set(keyPlayer, PersistentDataType.STRING, player);
		
		item.setItemMeta(meta);
		return item;
	}
	
	/**
	 * @since 1.0
	 */
	public static OfflinePlayer getOwner(ItemStack item) {
		if (item == null || !item.hasItemMeta())
			return null;
		if (!(item.getItemMeta() instanceof SkullMeta meta))
			return null;
		
		OfflinePlayer player = meta.getOwningPlayer();
		if(player != null)
			return player;
		
		PlayerProfile profile = meta.getOwnerProfile();
		if(profile != null)
			return Bukkit.getOfflinePlayer(profile.getUniqueId());
		
		String name = meta.getPersistentDataContainer().get(keyPlayer, PersistentDataType.STRING);
		if(name != null)
			return Bukkit.getOfflinePlayer(name);
			
		return null;
	}
	
	/**
	 * @since 1.0
	 */
	public static String getName(ItemStack item) {
		if (item == null || !item.hasItemMeta())
			return null;
		if (!(item.getItemMeta() instanceof SkullMeta meta))
			return null;
		String name = meta.getPersistentDataContainer().get(keyPlayer, PersistentDataType.STRING);
		if (name != null)
			return name;
		if (meta.getOwningPlayer() != null)
			return KamoofSMP.getInstance().getName(meta.getOwningPlayer());
		name = meta.getOwnerProfile().getName();
		if (name != null)
			return name;
		name = meta.getOwningPlayer().getName();
		return name;
	}
	
}
