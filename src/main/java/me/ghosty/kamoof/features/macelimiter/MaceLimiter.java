package me.ghosty.kamoof.features.macelimiter;

import me.ghosty.kamoof.KamoofSMP;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.*;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public final class MaceLimiter implements Listener {
	
	public static void add() {
		KamoofSMP.getData().set("maces", KamoofSMP.getData().getInt("maces", 0) + 1);
		KamoofSMP.saveData();
	}
	
	public static boolean canCraft() {
		return KamoofSMP.getData().getInt("maces", 0) < KamoofSMP.config().getInt("macelimiter.limit");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPrepareCraft(PrepareItemCraftEvent event) {
		ItemStack result = event.getInventory().getResult();
		if (event.getView().getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		if (result != null && result.getType() == Material.MACE)
			if (!canCraft())
				event.getInventory().setResult(null);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCraft(CraftItemEvent event) {
		if (event.getView().getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		if (event.getCurrentItem().getType() != Material.MACE)
			return;
		
		/*
		 * on évite les Shift+Clic par exemple, afin d'éviter que plusieurs soient craft d'un coup. un seul clic, pas +
		 */
		switch (event.getAction()) {
			case PICKUP_ALL:
			case PICKUP_HALF:
			case PICKUP_SOME:
			case PICKUP_ONE:
				break;
			default:
				event.setCancelled(true);
				return;
		}
		
		if (canCraft())
			add();
		else
			event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCrafter(CrafterCraftEvent event) {
		if (event.getResult().getType() != Material.MACE)
			return;
		
		if (canCraft())
			add();
		else
			event.setCancelled(true);
	}
	
}
