package me.ghosty.kamoof.features.ritual;

import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.structure.Structure;

import java.io.InputStream;
import java.util.Random;

public final class RitualSetup implements Listener {
	
	private static final NamespacedKey keyItem = new NamespacedKey("kamoofsmp", "setupitem");
	private static Structure structure;
	
	public static ItemStack[] getItems() {
		ItemStack item1 = new ItemStack(Material.NETHER_WART_BLOCK);
		ItemMeta meta1 = item1.getItemMeta();
		try {
			meta1.setItemName("§c§lSetup Rituel");
		} catch (Throwable exc) {
			meta1.setDisplayName("§c§lSetup Rituel");
		}
		meta1.addEnchant(Enchantment.UNBREAKING, 1, true);
		meta1.getPersistentDataContainer().set(keyItem, PersistentDataType.BOOLEAN, true);
		meta1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item1.setItemMeta(meta1);
		
		ItemStack item2 = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta2 = item2.getItemMeta();
		try {
			meta2.setItemName("§b§lPlace Rituel");
		} catch (Throwable exc) {
			meta2.setDisplayName("§b§lPlace Rituel");
		}
		meta2.addEnchant(Enchantment.UNBREAKING, 1, true);
		meta2.getPersistentDataContainer().set(keyItem, PersistentDataType.BOOLEAN, true);
		meta2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item2.setItemMeta(meta2);
		
		return new ItemStack[]{item1, item2};
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemInHand();
		if (!item.hasItemMeta() || item.getType() != Material.NETHER_WART_BLOCK || !item.getItemMeta().getPersistentDataContainer().has(keyItem, PersistentDataType.BOOLEAN))
			return;
		
		event.setCancelled(true);
		
		if (!player.hasPermission("kamoofsmp.admin"))
			return;
		
		Location loc = event.getBlockPlaced().getLocation();
		RitualHandler.setRitual(loc, player);
	}
	
	@SneakyThrows
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onUseItem(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if (item == null || !item.hasItemMeta() || item.getType() != Material.BLAZE_ROD || !item.getItemMeta().getPersistentDataContainer().has(keyItem, PersistentDataType.BOOLEAN))
			return;
		
		event.setCancelled(true);
		
		if (structure == null) {
			InputStream inputStream = getClass().getResourceAsStream("/ritual.nbt");
			structure = Bukkit.getStructureManager().loadStructure(inputStream);
		}
		structure.place(player.getLocation().add(-10, -8, -10), false, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
	}
	
}
