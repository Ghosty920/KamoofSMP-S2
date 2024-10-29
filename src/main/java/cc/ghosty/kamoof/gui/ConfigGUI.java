package cc.ghosty.kamoof.gui;

import cc.ghosty.kamoof.KamoofSMP;
import cc.ghosty.kamoof.utils.Lang;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static cc.ghosty.kamoof.KamoofSMP.*;

public final class ConfigGUI {
	
	public static SGMenu getMainMenu() {
		SGMenu menu = KamoofSMP.getSpiGUI().create("§a§lConfiguration du KamoofSMP", 3);
		
		/* Remplissage */
		{
			ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			ItemMeta meta = item.getItemMeta();
			meta.setHideTooltip(true);
			item.setItemMeta(meta);
			SGButton glassPaneBtn = new SGButton(item);
			for (int i = 0; i < 3 * 9; i++)
				menu.setButton(i, glassPaneBtn);
		}
		
		ItemStack itemLang = new ItemStack(Material.DIAMOND);
		ItemMeta metaLang = itemLang.getItemMeta();
		metaLang.addItemFlags(ItemFlag.values());
		metaLang.setDisplayName("§b§l" + Lang.languages.get(Lang.getLocale()));
		metaLang.setLore(Arrays.asList("§bClick to switch language"));
		itemLang.setItemMeta(metaLang);
		menu.setButton(10, new SGButton(itemLang).withListener(event -> {
			List<String> keys = Lang.languages.keySet().stream().toList();
			int index = (keys.indexOf(Lang.getLocale()) + 1) % keys.size();
			config().set("language", keys.get(index));
			KamoofSMP.getInstance().saveConfig();
			Lang.init();
			event.getWhoClicked().openInventory(getMainMenu().getInventory());
		}));
		
		ItemStack itemDisguise = new ItemStack(Material.IRON_INGOT);
		ItemMeta metaDisguise = itemDisguise.getItemMeta();
		metaDisguise.addItemFlags(ItemFlag.values());
		metaDisguise.setDisplayName("§f§lDisguise & cie.");
		itemDisguise.setItemMeta(metaDisguise);
//		menu.setButton(12, new SGButton(itemDisguise).withListener(event -> event.getWhoClicked().openInventory(getDisguiseMenu.getInventory())));
		
		ItemStack itemRitual = new ItemStack(Material.REDSTONE);
		ItemMeta metaRitual = itemRitual.getItemMeta();
		metaRitual.addItemFlags(ItemFlag.values());
		metaRitual.setDisplayName("§c§lRituel");
		itemRitual.setItemMeta(metaRitual);
//		menu.setButton(14, new SGButton(itemRitual).withListener(event -> event.getWhoClicked().openInventory(getRitualMenu().getInventory())));
		
		ItemStack itemOther = new ItemStack(Material.COAL);
		ItemMeta metaOther = itemOther.getItemMeta();
		metaOther.addItemFlags(ItemFlag.values());
		metaOther.setDisplayName("§7§lAutres");
		itemOther.setItemMeta(metaOther);
		menu.setButton(16, new SGButton(itemOther).withListener(event -> event.getWhoClicked().openInventory(getOtherMenu().getInventory())));
		
		ItemStack itemNote = new ItemStack(Material.LIGHT);
		ItemMeta metaNote = itemNote.getItemMeta();
		metaNote.addItemFlags(ItemFlag.values());
		metaNote.setDisplayName("§a§lNote");
		metaNote.setLore(Arrays.asList(
			"§aLes messages ne sont pas modifiables ici.",
			"§aVous pouvez depuis le fichier §7config.yml§a."
		));
		itemNote.setItemMeta(metaNote);
		menu.setButton(4, new SGButton(itemNote));
		
		return menu;
	}
	
	public static SGMenu getOtherMenu() {
		SGMenu menu = KamoofSMP.getSpiGUI().create("§a§lConfiguration: §7§lAutres", 3);
		/* Remplissage */
		{
			ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			ItemMeta meta = item.getItemMeta();
			meta.setHideTooltip(true);
			item.setItemMeta(meta);
			SGButton glassPaneBtn = new SGButton(item);
			for (int i = 0; i < 3 * 9; i++)
				menu.setButton(i, glassPaneBtn);
		}
		
		setButtonBoolean(menu, 10, "macelimiter.enabled", "Mace Limiter", "§7Ne pouvoir crafter qu'un", "§7nombre limité de maces.");
		setIntButton(menu, 11, "macelimiter.limit", value -> Math.max(0, value), "Limite de Maces", "§7La limite de maces craftable");
		
		setButtonBoolean(menu, 15, "autoupdate.fetch", "Auto Updater", "§7Vérifier pour une nouvelle", "§7mise à jour quotidiennement.");
		setButtonBoolean(menu, 16, "autoupdate.download", "Téléchargement automatique", "§7Télécharger automatiquement", "§7la mise à jour et proposer", "§7un redémarrage du serveur.");
		
		return menu;
	}
	
	private static void setButtonBoolean(SGMenu menu, int slot, String path, String name, String... lore) {
		boolean enabled = config().getBoolean(path);
		ItemStack item = new ItemStack(enabled ? Material.LIME_WOOL : Material.RED_WOOL);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName((enabled ? "§a" : "§c") + "§l" + name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		menu.setButton(slot, new SGButton(item).withListener(event -> {
			config().set(path, !enabled);
			getInstance().saveConfig();
			setButtonBoolean(menu, slot, path, name, lore);
			menu.refreshInventory(event.getWhoClicked());
		}));
	}
	
	private static void setIntButton(SGMenu menu, int slot, String path, Function<Integer, Integer> validate, String name, String... lore) {
		int value = config().getInt(path);
		ItemStack item = new ItemStack(Material.YELLOW_WOOL);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§e§l" + name + ": §6§l"+value);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		menu.setButton(slot, new SGButton(item).withListener(event -> {
			int newValue = value;
			switch (event.getClick()) {
				case LEFT -> newValue += 1;
				case RIGHT -> newValue -= 1;
				// ne fonctionne pas car on n'autorise pas les shift clics, mais là au caou ig..
				case SHIFT_LEFT -> newValue += 3;
				case SHIFT_RIGHT -> newValue -= 3;
			}
			newValue = validate.apply(newValue);
			config().set(path, newValue);
			getInstance().saveConfig();
			setIntButton(menu, slot, path, validate, name, lore);
			menu.refreshInventory(event.getWhoClicked());
		}));
	}
	
}
