package im.ghosty.kamoof.features.macelimiter;

import im.ghosty.kamoof.KamoofPlugin;
import im.ghosty.kamoof.features.Feature;
import im.ghosty.kamoof.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class MaceNoEnderChest extends Feature {
	
	@Override
	public boolean isEnabled() {
		return KamoofPlugin.config().getBoolean("macelimiter.no-enderchest") && Material.getMaterial("MACE") != null;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event) {
		InventoryAction action = event.getAction();
		if (action == InventoryAction.NOTHING || action == InventoryAction.UNKNOWN)
			return;
		
		ItemStack item = event.getCursor();
		Inventory inv = event.getClickedInventory();
		
		if (inv.getType() == InventoryType.ENDER_CHEST) {
			if (action == InventoryAction.HOTBAR_SWAP && item.getType() == Material.AIR) {
				item = event.getCurrentItem();
				if (item.getType() == Material.AIR) { // si qql a un fix, merci dm'aider ;(
					event.setCancelled(true);
					event.setResult(Event.Result.DENY);
					return;
				}
			} else if (action != InventoryAction.PLACE_ALL
				&& action != InventoryAction.PLACE_SOME
				&& action != InventoryAction.PLACE_ONE
				&& action != InventoryAction.SWAP_WITH_CURSOR)
				return;
		} else {
			if (event.getInventory().getType() != InventoryType.ENDER_CHEST)
				return;
			if (action != InventoryAction.MOVE_TO_OTHER_INVENTORY)
				return;
			item = event.getCurrentItem();
		}
		
		boolean flags = InventoryUtils.hasItem(item, 2, Material.MACE);
		if (flags) {
			event.setCancelled(true);
			event.setResult(Event.Result.DENY);
		}
	}
	
}
