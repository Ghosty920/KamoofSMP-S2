package me.ghosty.kamoof.features.ritual;

import me.ghosty.kamoof.KamoofSMP;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.joml.Vector2d;

import java.util.ArrayList;

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
		
		Location loc = event.getBlockPlaced().getLocation();
		
		ArrayList<Integer> entities = new ArrayList<>();
		for (Vector2d offset : RitualHandler.offsets) {
			double x = loc.getX() + offset.x + 0.5, z = loc.getZ() + offset.y + 0.5;
			player.spawnParticle(Particle.DUST, x, loc.getY() + 0.5, z, 4, 0, 0, 0, 0, (new Particle.DustOptions(Color.YELLOW, 2)), true);
		
			Location location = new Location(player.getWorld(), x, loc.getY(), z);
			ArmorStand entity = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			entity.setArms(false);
			entity.setBasePlate(false);
			entity.setVisible(false);
			entity.setCollidable(false);
			entity.setInvulnerable(true);
			entity.setGravity(false);
			entity.setAI(false);
			entities.add(entity.getEntityId());
		}
		
		player.sendMessage(KamoofSMP.PREFIX + String.format("§aNouveau lieu de Rituel: %s %s %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		KamoofSMP.getInstance().reloadConfig();
		KamoofSMP.config().set("ritual.data.location", loc.add(0.5, 0.5, 0.5));
		KamoofSMP.config().set("ritual.data.entities", entities);
		KamoofSMP.getInstance().saveConfig();
	}
	
}
