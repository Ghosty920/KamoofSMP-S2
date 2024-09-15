package me.ghosty.kamoof.features.drophead;

import lombok.experimental.UtilityClass;
import me.ghosty.kamoof.KamoofSMP;
import me.ghosty.kamoof.utils.Placeholder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.Map;

@UtilityClass
public final class SkullManager {
	
	private static final NamespacedKey keyTimestamp = new NamespacedKey("kamoofsmp", "timestamp");
	private static final NamespacedKey keyPlayer = new NamespacedKey("kamoofsmp", "player");
	
	public static ItemStack getSkull(String player) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(player);
		meta.setOwningPlayer(target);
//		meta.setOwnerProfile(Bukkit.createPlayerProfile(offlinePlayer.getUniqueId()));
		
		meta.setItemName(Placeholder.apply(KamoofSMP.config().getString("drophead.name"), Map.of("player", player)));
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
		return meta.getOwningPlayer();
	}
	
	/**
	 * looks shit but works fine for now
	 */
	public static String getName(ItemStack item) {
		if (item == null || !item.hasItemMeta())
			return null;
		if (!(item.getItemMeta() instanceof SkullMeta meta))
			return null;
		String name = meta.getPersistentDataContainer().get(keyPlayer, PersistentDataType.STRING);
		if(name != null)
			return name;
		if(meta.getOwningPlayer() != null && meta.getOwningPlayer() instanceof Player player)
			return NickAPI.getOriginalName(player);
		name = meta.getOwnerProfile().getName();
		if(name != null)
			return name;
		name = meta.getOwningPlayer().getName();
		return name;
	}
}
