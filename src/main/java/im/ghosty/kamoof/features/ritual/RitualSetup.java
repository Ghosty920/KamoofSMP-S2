package im.ghosty.kamoof.features.ritual;

import im.ghosty.kamoof.features.Feature;
import im.ghosty.kamoof.utils.*;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.structure.Structure;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

import static im.ghosty.kamoof.KamoofPlugin.config;

/**
 * {@link Feature} pour gérer la mise en place du Rituel.
 */
public final class RitualSetup extends Feature {
	
	private static final NamespacedKey keyItem = new NamespacedKey("kamoofsmp", "setupitem");
	private static Structure structure, structureBase;
	
	public static ItemStack[] getItems() {
		return new ItemStack[]{
			getItem(Material.NETHER_WART_BLOCK, "FIRST"),
			getItem(Material.BREEZE_ROD, "SECOND"),
			getItem(Material.BLAZE_ROD, "THIRD")
		};
	}
	
	private static ItemStack getItem(Material material, String pos) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		
		String name = Lang.get("SETUP_ITEM_" + pos);
		try {
			meta.setItemName(name);
		} catch (Throwable exc) {
			meta.setDisplayName(name);
		}
		meta.setLore(Arrays.asList(Lang.get("SETUP_ITEM_" + pos + "_DESC").split("<br>")));
		
		meta.addEnchant(Enchantment.FROST_WALKER, 1, true);
		meta.getPersistentDataContainer().set(keyItem, PersistentDataType.BOOLEAN, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public boolean isEnabled() {
		return CompatibilityUtils.isMinecraft1_21()
			&& config().getBoolean("ritual.enabled");
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
		if (item == null || !item.hasItemMeta() || (item.getType() != Material.BREEZE_ROD && item.getType() != Material.BLAZE_ROD) || !item.getItemMeta().getPersistentDataContainer().has(keyItem, PersistentDataType.BOOLEAN))
			return;
		
		event.setCancelled(true);
		
		if (!player.hasPermission("kamoofsmp.admin"))
			return;
		
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		switch (item.getType()) {
			case BREEZE_ROD -> {
				if (structure == null) {
					InputStream inputStream = getClass().getResourceAsStream("/ritual.nbt");
					structure = Bukkit.getStructureManager().loadStructure(inputStream);
				}
				// 27 / 11 / 27
				SLocation loc = new SLocation(event.getClickedBlock().getLocation());
				structure.place(loc.plus(-13, -1, -13), false, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
				RitualHandler.setRitual(loc.plus(0, 1, 0).toLocation(), player);
			}
			case BLAZE_ROD -> {
				if (structureBase == null) {
					InputStream inputStream = getClass().getResourceAsStream("/ritualbase.nbt");
					structureBase = Bukkit.getStructureManager().loadStructure(inputStream);
				}
				// 19 / 11 / 19
				SLocation loc = new SLocation(event.getClickedBlock().getLocation());
				structureBase.place(loc.plus(-9, 0, -9), false, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
				RitualHandler.setRitual(loc.plus(0, 2, 0).toLocation(), player);
			}
		}
	}
	
}
