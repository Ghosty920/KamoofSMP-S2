package me.ghosty.kamoof.features.ritual;

import com.google.common.base.Joiner;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;

public final class RitualSetup implements Listener {
	
	private static final NamespacedKey keyItem = new NamespacedKey("kamoofsmp", "setupitem");
	
	public static ItemStack getItem() {
		ItemStack item = new ItemStack(Material.NETHER_WART_BLOCK);
		ItemMeta meta = item.getItemMeta();
		meta.setItemName("§c§lSetup Rituel");
		meta.addEnchant(Enchantment.UNBREAKING, 1, true);
		meta.getPersistentDataContainer().set(keyItem, PersistentDataType.BOOLEAN, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		return item;
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemInHand();
		if(!item.hasItemMeta() || !item.getItemMeta().getPersistentDataContainer().has(keyItem, PersistentDataType.BOOLEAN))
			return;
		event.setCancelled(true);
		
		player.sendMessage("ur beautiful ma vie!!!");
	}
	
}
