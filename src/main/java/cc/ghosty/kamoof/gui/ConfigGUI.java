package cc.ghosty.kamoof.gui;

import cc.ghosty.kamoof.KamoofSMP;
import cc.ghosty.kamoof.utils.ColorResolver;
import cc.ghosty.kamoof.utils.Lang;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

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
		metaLang.setLore(Arrays.asList("§bClick to switch language."));
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
		menu.setButton(12, new SGButton(itemDisguise).withListener(event -> event.getWhoClicked().openInventory(getDisguiseMenu().getInventory())));
		
		ItemStack itemRitual = new ItemStack(Material.REDSTONE);
		ItemMeta metaRitual = itemRitual.getItemMeta();
		metaRitual.addItemFlags(ItemFlag.values());
		metaRitual.setDisplayName("§c§lRituel");
		itemRitual.setItemMeta(metaRitual);
		menu.setButton(14, new SGButton(itemRitual).withListener(event -> event.getWhoClicked().openInventory(getRitualMenu().getInventory())));
		
		ItemStack itemOther = new ItemStack(Material.COAL);
		ItemMeta metaOther = itemOther.getItemMeta();
		metaOther.addItemFlags(ItemFlag.values());
		metaOther.setDisplayName("§7§lAutres");
		itemOther.setItemMeta(metaOther);
		menu.setButton(16, new SGButton(itemOther).withListener(event -> event.getWhoClicked().openInventory(getOtherMenu().getInventory())));
		
		setButtonNote(menu, 4, "§aLes messages ne sont pas modifiables ici.", "§aVous pouvez depuis le fichier §7config.yml§a.");
		
		return menu;
	}
	
	public static SGMenu getDisguiseMenu() {
		SGMenu menu = KamoofSMP.getSpiGUI().create("§a§lConfiguration: §f§lDisguise & Cie.", 3);
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
		
		setButtonMainMenu(menu, 4);
		
		setButtonBool(menu, 10, "disguise.place-head", "Têtes placeables", "§7Le fait de pouvoir poser", "§7les têtes de joueur.");
		setButtonBool(menu, 11, "disguise.give-back", "Rendre la tête", "§7Le fait de récupérer la", "§7tête de son déguisement quand", "§7on en change ou qu'on utilise", "§7la commande /undisguise.");
		
		setButtonBool(menu, 13, "restaure.enabled", "Restaure", "§7Le fait de récupérer son", "§7déguisement en se reconnectant", "§7au serveur, après un", "§7redémarrage, etc.");
		
		setButtonBool(menu, 15, "drophead.enabled", "Drop Head", "§7Faire que la tête du joueur", "§7tombe à sa mort.");
		setButtonBool(menu, 16, "drophead.stackable", "Stackable", "§7Rendre les têtes stackables.", "§7Fonctionne davantage avec", "§3Stackable Heads §7créé par", "§7Solme, disponible sur Modrinth.");
		
		return menu;
	}
	
	public static SGMenu getRitualMenu() {
		SGMenu menu = KamoofSMP.getSpiGUI().create("§a§lConfiguration: §c§lRituel", 6);
		/* Remplissage */
		{
			ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			ItemMeta meta = item.getItemMeta();
			meta.setHideTooltip(true);
			item.setItemMeta(meta);
			SGButton glassPaneBtn = new SGButton(item);
			for (int i = 0; i < 6 * 9; i++)
				menu.setButton(i, glassPaneBtn);
		}
		
		setButtonMainMenu(menu, 4);
		
		setButtonBool(menu, 9, "ritual.enabled", "Rituel", "§7Activer ou désactiver la", "§7mécanique du Rituel et", "§7des deux Pactes.");
		setButtonInt(menu, 10, "ritual.dupelimit", value -> Math.max(0, Math.min(9, value)), 1, "Limite de Doublons", "§7Le nombre maximal de têtes", "§7identiques acceptées pour", "§7lancer un rituel.");
		setButtonInt(menu, 11, "ritual.min-time", value -> Math.max(0, Math.min(value, Math.min(24000, config().getInt("ritual.max-time")))), 500, "Temps Minimum", "§7Le temps minimum pour lancer", "§7le rituel. Peut se tester", "§7avec /time set <nombre>");
		setButtonInt(menu, 12, "ritual.max-time", value -> Math.min(24000, Math.max(value, Math.max(0, config().getInt("ritual.min-time")))), 500, "Temps Maximum", "§7Le temps maximum pour lancer", "§7le rituel. Peut se tester", "§7avec /time set <nombre>");
		
		setButtonInt(menu, 14, "ritual.pactes.bloody.hpboost", value -> Math.max(0, value), 1, "§4Pacte Ensanglanté: §e§lHP Boost", "§7Le boost de HP quand le joueur", "§7accepte le pacte ensanglanté.", "§7Note: Diviser par 2 pour obtenir en ❤.", "§7Note: Vous devrez relancer le serveur.");
		setButtonInt(menu, 15, "ritual.pactes.bloody.heads", value -> Math.max(0, Math.min(64, value)), 1, "§4Pacte Ensanglanté: §e§lTêtes", "§7Le nombre de tête à la mort", "§7d'un joueur qui a accepté", "§7le pacte ensanglanté.");
		setButtonInt(menu, 17, "ritual.pactes.forgotten.weakness", value -> Math.max(0, Math.min(255, value)), 1, "§8Pacte Oublié: §e§lFaiblesse", "§7Le niveau de faiblesse donné", "§7à un joueur qui accepte", "§7le pacte oublié.");
		
		setButtonValues(
			menu, 30, "ritual.animation.time-incr",
			Arrays.asList(1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 16, 18, 20, 24, 25, 30, 36, 40, 45, 48, 50, 60, 72, 75, 80, 90, 100, 120, 125, 144, 150, 180, 200, 225, 240, 250, 300, 360, 375, 400, 450, 500, 600, 720, 750, 900, 1000, 1125, 1200, 1500, 1800, 2000, 2250, 3000, 3600, 4500, 6000, 9000, 18000),
			"Incrémentation du Temps",
			"§7La vitesse à laquelle le", "§7temps passe à la nuit."
		);
		setButtonString(menu, 31, "ritual.animation.color", value -> ColorResolver.getColor(value) != null, "Couleur des Particules", "§7La couleur des particules", "§7qui font toute la structure", "§7de l'animation du rituel.");
		setButtonDouble(menu, 32, "ritual.animation.size", value -> Math.max(0.1, Math.min(10, value)), 0.1, "Taille des Particules", "§7La taille des particules", "§7qui font toute la structure", "§7de l'animation du rituel.");
		
		setButtonDouble(menu, 37, "ritual.animation.sphere.radius", value -> Math.max(0.5, Math.min(5, value)), 0.1, "Sphère: Rayon", "§7Le rayon de la sphère.");
		setButtonInt(menu, 38, "ritual.animation.sphere.particles", value -> Math.max(0, value), 50, "Sphère: Quantité", "§7Le nombre de particules de la sphère.");
		setButtonString(menu, 39, "ritual.animation.sphere.color", value -> ColorResolver.getColor(value) != null, "Sphère: Couleur", "§7La couleur des particules de la sphère.");
		setButtonNote(menu, 40, "§aLa \"sphère\" est la sphère", "§aqui apparaît durant l'animation", "§adu rituel, vers la fin, au", "§acentre de la structure.");
		setButtonDouble(menu, 41, "ritual.animation.sphere.size", value -> Math.max(0.1, Math.min(10, value)), 0.1, "Sphère: Taille", "§7La taille des particules de la sphère.");
		setButtonInt(menu, 42, "ritual.animation.sphere.lava-chance", value -> Math.max(0, Math.min(100, value)), 1, "Sphère: Lave %", "§7La chance qu'une particule", "§7de la sphère soit de lave.");
		setButtonBool(menu, 43, "ritual.animation.sphere.lava-sound", "Sphère: Bruit de Lave", "§7Si les particules de lave", "§7de la sphère doivent faire", "§7du bruit en tombant.");
		
		setButtonDouble(menu, 46, "ritual.accepted.lava-radius", value -> Math.max(0.5, Math.min(5, value)), 0.1, "Accepté: Rayon Lave", "§7Le rayon de la sphère de lave.");
		setButtonInt(menu, 47, "ritual.accepted.lava-particles", value -> Math.max(0, value), 50, "Accepté: Quantité Lave", "§7Le nombre de particules de", "§7lave de la sphère.");
		setButtonBool(menu, 48, "ritual.accepted.lava-sound", "Accepté: Bruit de Lave", "§7Si les particules de lave", "§7de la sphère doivent faire", "§7du bruit en tombant.");
		setButtonNote(menu, 49, "§aLe fait \"d'accepter\" un", "§apacte est le fait de cliquer", "§asur accepter sur une page du", "§alivre donné à la fin du rituel.", " ", "§aIci on définit l'animation", "§aquand on accepte un pacte.");
		setButtonInt(menu, 50, "ritual.accepted.flame-particles", value -> Math.max(0, value), 50, "Accepté: Quantité Flammes", "§7Le nombre de particules de flamme.");
		setButtonDouble(menu, 51, "ritual.accepted.flame-speed", value -> Math.max(0.1, Math.min(5, value)), 0.1, "Accepté: Vitesse Flammes", "§7La vitesse des particules de flamme.");
		setButtonBool(menu, 52, "ritual.accepted.flame-soul", "Accepté: Flammes des âmes", "§7Rendre les particules de", "§7flamme en flammes des âmes.");
		
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
		
		setButtonMainMenu(menu, 4);
		
		setButtonBool(menu, 10, "macelimiter.enabled", "Mace Limiter", "§7Ne pouvoir crafter qu'un", "§7nombre limité de maces.");
		setButtonInt(menu, 11, "macelimiter.limit", value -> Math.max(0, value), 1, "Limite de Maces", "§7La limite de maces craftable.");
		
		setButtonBool(menu, 15, "autoupdate.fetch", "Auto Updater", "§7Vérifier pour une nouvelle", "§7mise à jour quotidiennement.");
		setButtonBool(menu, 16, "autoupdate.download", "Téléchargement automatique", "§7Télécharger automatiquement", "§7la mise à jour et proposer", "§7un redémarrage du serveur.");
		
		return menu;
	}
	
	private static void setButtonMainMenu(SGMenu menu, int slot) {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§7Retour au Menu");
		item.setItemMeta(meta);
		menu.setButton(slot, new SGButton(item).withListener(event -> event.getWhoClicked().openInventory(getMainMenu().getInventory())));
	}
	
	private static void setButtonNote(SGMenu menu, int slot, String... lore) {
		ItemStack item = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§a§lNote");
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		menu.setButton(slot, new SGButton(item));
	}
	
	private static void setButtonBool(SGMenu menu, int slot, String path, String name, String... lore) {
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
			setButtonBool(menu, slot, path, name, lore);
			menu.refreshInventory(event.getWhoClicked());
		}));
	}
	
	private static void setButtonInt(SGMenu menu, int slot, String path, Function<Integer, Integer> validate, int incr, String name, String... lore) {
		int value = config().getInt(path);
		ItemStack item = new ItemStack(Material.YELLOW_WOOL, Math.max(1, Math.min(64, (int) Math.floor((double) value / incr))));
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§e§l" + name + ": §6§l" + value);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		menu.setButton(slot, new SGButton(item).withListener(event -> {
			int newValue = value;
			switch (event.getClick()) {
				case LEFT -> newValue += incr;
				case RIGHT -> newValue -= incr;
			}
			newValue = validate.apply(newValue);
			config().set(path, newValue);
			getInstance().saveConfig();
			setButtonInt(menu, slot, path, validate, incr, name, lore);
			menu.refreshInventory(event.getWhoClicked());
		}));
	}
	
	private static void setButtonDouble(SGMenu menu, int slot, String path, Function<Double, Double> validate, double incr, String name, String... lore) {
		double value = config().getDouble(path);
		ItemStack item = new ItemStack(Material.ORANGE_WOOL, Math.max(1, Math.min(64, (int) Math.floor(value / incr))));
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§e§l" + name + ": §6§l" + value);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		menu.setButton(slot, new SGButton(item).withListener(event -> {
			double newValue = value;
			switch (event.getClick()) {
				case LEFT -> newValue += incr;
				case RIGHT -> newValue -= incr;
			}
			newValue = (double) Math.round(newValue * 1000) / 1000;
			newValue = validate.apply(newValue);
			config().set(path, newValue);
			getInstance().saveConfig();
			setButtonDouble(menu, slot, path, validate, incr, name, lore);
			menu.refreshInventory(event.getWhoClicked());
		}));
	}
	
	private static void setButtonValues(SGMenu menu, int slot, String path, List<?> values, String name, String... lore) {
		Object value = config().get(path);
		ItemStack item = new ItemStack(Material.WHITE_WOOL);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§e§l" + name + ": §6§l" + value);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		menu.setButton(slot, new SGButton(item).withListener(event -> {
			int index = 0;
			switch (event.getClick()) {
				case LEFT -> {
					try {
						index = (values.indexOf(value) + 1) % values.size();
					} catch (Throwable exc) {
						index = 0;
					}
				}
				case RIGHT -> {
					try {
						index = (values.indexOf(value) - 1) % values.size();
						if(index < 0)
							index = values.size() - 1;
					} catch (Throwable exc) {
						index = 0;
					}
				}
			}
			
			config().set(path, values.get(index));
			getInstance().saveConfig();
			setButtonValues(menu, slot, path, values, name, lore);
			menu.refreshInventory(event.getWhoClicked());
		}));
	}
	
	private static void setButtonString(SGMenu menu, int slot, String path, Predicate<String> predicate, String name, String... lore) {
		String value = config().getString(path);
		ItemStack item = new ItemStack(Material.CYAN_WOOL);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§3§l" + name + ": §6§l" + value);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		menu.setButton(slot, new SGButton(item).withListener(event -> {
			StringGUI gui = new StringGUI("§3§l" + name, value, predicate)
				.setOnAccept(ev -> {
					config().set(path, ev.getCursor().getItemMeta().getDisplayName());
				})
				.setOnClose(ev -> ev.getPlayer().openInventory(menu.getInventory()));
			event.getWhoClicked().openInventory(gui.getInventory());
		}));
	}
	
	private static ArrayList<String> getLore(String key) {
		ArrayList<String> lore = new ArrayList<>();
		for (String line : Lang.get(key).split("\\|"))
			lore.add("§7" + line);
		return lore;
	}
	
}
