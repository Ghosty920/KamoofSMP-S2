package im.ghosty.kamoof.features.macelimiter;

import im.ghosty.kamoof.KamoofPlugin;
import im.ghosty.kamoof.features.Feature;
import im.ghosty.kamoof.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import xyz.haoshoku.nick.NickAPI;

import java.util.Map;

import static im.ghosty.kamoof.KamoofPlugin.*;

/**
 * {@link Feature} pour empêcher trop de maces d'être craftées
 * @since 1.0
 */
public final class MaceLimiter extends Feature {
	
	public Listener listener;
	
	// fix un bug avec certaines versions spigot
	public MaceLimiter() {
		try {
			listener = new Listener() {
				
				@EventHandler(priority = EventPriority.HIGHEST)
				public void onCrafter(CrafterCraftEvent event) {
					if (event.getResult().getType() != Material.MACE)
						return;
					if (canCraft()) add();
					else event.setCancelled(true);
				}
				
			};
		} catch (Throwable exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * Ajouter qu'une mace craftée au total
	 */
	public static void add() {
		data().set("maces", data().getInt("maces", 0) + 1);
		saveData();
	}
	
	/**
	 * @return Si une nouvelle mace peut être craftée
	 */
	public static boolean canCraft() {
		return data().getInt("maces", 0) < KamoofPlugin.config().getInt("macelimiter.limit");
	}
	
	@Override
	public boolean isEnabled() {
		return KamoofPlugin.config().getBoolean("macelimiter.enabled") && Material.getMaterial("MACE") != null;
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		if (listener != null)
			HandlerList.unregisterAll(listener);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		if (listener != null)
			Bukkit.getPluginManager().registerEvents(listener, KamoofPlugin.getInstance());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPrepareCraft(PrepareItemCraftEvent event) {
		ItemStack result = event.getInventory().getResult();
//		if (event.getView().getPlayer().getGameMode() == GameMode.CREATIVE)
//			return;
		if (result != null && result.getType() == Material.MACE && !canCraft())
			event.getInventory().setResult(null);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCraft(CraftItemEvent event) {
//		if (event.getView().getPlayer().getGameMode() == GameMode.CREATIVE)
//			return;
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
		
		if (canCraft()) {
			int old = data().getInt("maces", 0);
			int limit = KamoofPlugin.config().getInt("macelimiter.limit");
			add();
			HumanEntity player = event.getView().getPlayer();
			Message.send(player, "messages.craft-mace", Map.of("player", (player instanceof Player p ? NickAPI.getOriginalName(p) : player.getName()), "old", old, "now", old + 1, "limit", limit));
		} else
			event.setCancelled(true);
	}
	
}
