package me.ghosty.kamoof.features.drophead;

import lombok.experimental.UtilityClass;
import me.ghosty.kamoof.KamoofSMP;
import me.ghosty.kamoof.utils.Placeholder;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

@UtilityClass
public final class SkullManager {
	
	private static final NamespacedKey keyTimestamp = new NamespacedKey("kamoofsmp", "timestamp");
	private static final NamespacedKey keyPlayer = new NamespacedKey("kamoofsmp", "player");
	
	public static ItemStack getSkull(String player) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(player));
		try {
			meta.setItemName(Placeholder.apply(KamoofSMP.config().getString("drophead.name"), Map.of("player", player)));
		} catch (Throwable exc) {
			meta.setDisplayName(Placeholder.apply(KamoofSMP.config().getString("drophead.name"), Map.of("player", player)));
		}
		meta.setLore(Placeholder.apply(KamoofSMP.config().getStringList("drophead.lore"), Map.of("player", player)));
		
		boolean stackable = KamoofSMP.config().getBoolean("drophead.stackable");
		meta.getPersistentDataContainer().set(keyTimestamp, PersistentDataType.LONG, stackable ? -1L : System.currentTimeMillis());
		meta.getPersistentDataContainer().set(keyPlayer, PersistentDataType.STRING, player);
		
		item.setItemMeta(meta);
		return item;
	}
	
	public static OfflinePlayer getOwner(ItemStack item) {
		if (item == null || !item.hasItemMeta())
			return null;
		if (!(item.getItemMeta() instanceof SkullMeta meta))
			return null;
		if (!meta.getPersistentDataContainer().has(keyTimestamp, PersistentDataType.LONG))
			return null;
		return meta.getOwningPlayer();
	}
	
	public static String getName(ItemStack item) {
		if (item == null || !item.hasItemMeta())
			return null;
		if (!(item.getItemMeta() instanceof SkullMeta meta))
			return null;
		return meta.getPersistentDataContainer().get(keyPlayer, PersistentDataType.STRING);
	}
}
