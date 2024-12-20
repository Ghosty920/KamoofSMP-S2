package im.ghosty.kamoof.commands;

import im.ghosty.kamoof.KamoofPlugin;
import im.ghosty.kamoof.features.FeatureManager;
import im.ghosty.kamoof.utils.*;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;
import net.md_5.bungee.api.ChatMessageType;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static im.ghosty.kamoof.KamoofPlugin.*;

/**
 * Menu de Configuration en-jeu global du KamoofSMP.
 * <p>
 * Ouvrable avec <code>/kamoofsmp config</code> en jeu.
 * @since 1.4
 */
public final class ConfigGUI {
	
	/**
	 * @return Le {@link SGMenu} principal
	 */
	public static SGMenu getMainMenu() {
		SGMenu menu = KamoofPlugin.getSpiGUI().create("§a§l" + Lang.get("CFG_TITLE_MAIN"), 3);
		
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
		metaLang.setLore(List.of("§bClick to switch language."));
		itemLang.setItemMeta(metaLang);
		menu.setButton(10, new SGButton(itemLang).withListener(event -> {
			List<String> keys = Lang.languages.keySet().stream().toList();
			int index = (keys.indexOf(Lang.getLocale()) + 1) % keys.size();
			config().set("language", keys.get(index));
			KamoofPlugin.getInstance().saveConfig();
			Lang.init();
			event.getWhoClicked().openInventory(getMainMenu().getInventory());
		}));
		
		ItemStack itemDisguise = new ItemStack(Material.IRON_INGOT);
		ItemMeta metaDisguise = itemDisguise.getItemMeta();
		metaDisguise.addItemFlags(ItemFlag.values());
		metaDisguise.setDisplayName("§f§l" + Lang.get("CFG_TITLE_DISGUISE"));
		itemDisguise.setItemMeta(metaDisguise);
		menu.setButton(12, new SGButton(itemDisguise).withListener(event -> event.getWhoClicked().openInventory(getDisguiseMenu().getInventory())));
		
		ItemStack itemRitual = new ItemStack(Material.REDSTONE);
		ItemMeta metaRitual = itemRitual.getItemMeta();
		metaRitual.addItemFlags(ItemFlag.values());
		metaRitual.setDisplayName("§c§l" + Lang.get("CFG_TITLE_RITUAL"));
		itemRitual.setItemMeta(metaRitual);
		menu.setButton(14, new SGButton(itemRitual).withListener(event -> event.getWhoClicked().openInventory(getRitualMenu().getInventory())));
		
		ItemStack itemOther = new ItemStack(Material.COAL);
		ItemMeta metaOther = itemOther.getItemMeta();
		metaOther.addItemFlags(ItemFlag.values());
		metaOther.setDisplayName("§7§l" + Lang.get("CFG_TITLE_OTHER"));
		itemOther.setItemMeta(metaOther);
		menu.setButton(16, new SGButton(itemOther).withListener(event -> event.getWhoClicked().openInventory(getOtherMenu().getInventory())));
		
		setButtonNote(menu, 4, "CFG_NOTE_MESSAGES");
		
		return menu;
	}
	
	/**
	 * @return Le {@link SGMenu} des paramètres de Disguise et Drop Head
	 */
	public static SGMenu getDisguiseMenu() {
		SGMenu menu = KamoofPlugin.getSpiGUI().create("§a§l" + Lang.get("CFG_TITLE") + ": §f§l" + Lang.get("CFG_TITLE_DISGUISE"), 3);
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
		
		setButtonBool(menu, 10, "disguise.place-head", getStrings("CFG_DISGUISE_PLACEHEAD"));
		setButtonBool(menu, 11, "disguise.give-back", getStrings("CFG_DISGUISE_GIVEBACK"));
		
		setButtonBool(menu, 13, "disguise.restaure", getStrings("CFG_RESTAURE"));
		
		setButtonBool(menu, 15, "drophead.enabled", getStrings("CFG_DROPHEAD_ENABLED"));
		setButtonBool(menu, 16, "drophead.stackable", getStrings("CFG_DROPHEAD_STACKABLE"));
		
		return menu;
	}
	
	/**
	 * @return Le {@link SGMenu} des paramètres du Rituel
	 */
	public static SGMenu getRitualMenu() {
		SGMenu menu = KamoofPlugin.getSpiGUI().create("§a§l" + Lang.get("CFG_TITLE") + ": §c§l" + Lang.get("CFG_TITLE_RITUAL"), 6);
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
		
		setButtonBool(menu, 9, "ritual.enabled", getStrings("CFG_RITUAL_ENABLED"));
		setButtonInt(menu, 10, "ritual.dupelimit", value -> Math.max(1, Math.min(9, value)), 1, getStrings("CFG_RITUAL_DUPELIMIT"));
		setButtonInt(menu, 11, "ritual.min-time", value -> Math.max(0, Math.min(value, Math.min(24000, config().getInt("ritual.max-time")))), 500, getStrings("CFG_RITUAL_MINTIME"));
		setButtonInt(menu, 12, "ritual.max-time", value -> Math.min(24000, Math.max(value, Math.max(0, config().getInt("ritual.min-time")))), 500, getStrings("CFG_RITUAL_MAXTIME"));
		
		setButtonInt(menu, 14, "ritual.pactes.bloody.hpboost", value -> Math.max(0, value), 1, getStrings("CFG_RITUAL_BLOODY_HPBOOST"));
		setButtonInt(menu, 15, "ritual.pactes.bloody.heads", value -> Math.max(0, Math.min(64, value)), 1, getStrings("CFG_RITUAL_BLOODY_HEADS"));
		setButtonInt(menu, 17, "ritual.pactes.forgotten.weakness", value -> Math.max(0, Math.min(255, value)), 1, getStrings("CFG_RITUAL_FORGOTTEN_WEAKNESS"));
		
		setButtonValues(
			menu, 28, "ritual.animation.time-incr",
			Arrays.asList(1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 16, 18, 20, 24, 25, 30, 36, 40, 45, 48, 50, 60, 72, 75, 80, 90, 100, 120, 125, 144, 150, 180, 200, 225, 240, 250, 300, 360, 375, 400, 450, 500, 600, 720, 750, 900, 1000, 1125, 1200, 1500, 1800, 2000, 2250, 3000, 3600, 4500, 6000, 9000, 18000),
			getStrings("CFG_RITUAL_ANIM_TIMEINCR")
		);
		setButtonColor(menu, 29, "ritual.animation.color", getStrings("CFG_RITUAL_ANIM_COLOR"));
		setButtonDouble(menu, 30, "ritual.animation.size", value -> Math.max(0.1, Math.min(10, value)), 0.1, getStrings("CFG_RITUAL_ANIM_SIZE"));
		setButtonInt(menu, 32, "ritual.animation.lightning-quantity", value -> Math.max(0, value), 1, getStrings("CFG_RITUAL_ANIM_LIGHTNING_QT"));
		setButtonInt(menu, 33, "ritual.animation.lightning-interval", value -> Math.max(0, value), 1, getStrings("CFG_RITUAL_ANIM_LIGHTNING_INTERVAL"));
		
		setButtonDouble(menu, 37, "ritual.animation.sphere.radius", value -> Math.max(0.5, Math.min(5, value)), 0.1, getStrings("CFG_RITUAL_SPH_RADIUS"));
		setButtonInt(menu, 38, "ritual.animation.sphere.quantity", value -> Math.max(0, value), 50, getStrings("CFG_RITUAL_SPH_QT"));
		setButtonColor(menu, 39, "ritual.animation.sphere.color", getStrings("CFG_RITUAL_SPH_COLOR"));
		setButtonNote(menu, 40, "CFG_RITUAL_SPH_NOTE");
		setButtonDouble(menu, 41, "ritual.animation.sphere.size", value -> Math.max(0.1, Math.min(10, value)), 0.1, getStrings("CFG_RITUAL_SPH_SIZE"));
		setButtonInt(menu, 42, "ritual.animation.sphere.lava-chance", value -> Math.max(0, Math.min(100, value)), 1, getStrings("CFG_RITUAL_SPH_LAVACHANCE"));
		setButtonBool(menu, 43, "ritual.animation.sphere.lava-sound", getStrings("CFG_RITUAL_SPH_LAVASOUND"));
		
		setButtonDouble(menu, 46, "ritual.accepted.lava-radius", value -> Math.max(0.5, Math.min(5, value)), 0.1, getStrings("CFG_RITUAL_ACC_LAVARADIUS"));
		setButtonInt(menu, 47, "ritual.accepted.lava-quantity", value -> Math.max(0, value), 50, getStrings("CFG_RITUAL_ACC_LAVAQT"));
		setButtonBool(menu, 48, "ritual.accepted.lava-sound", getStrings("CFG_RITUAL_ACC_LAVASOUND"));
		setButtonNote(menu, 49, "CFG_RITUAL_ACC_NOTE");
		setButtonInt(menu, 50, "ritual.accepted.flame-quantity", value -> Math.max(0, value), 50, getStrings("CFG_RITUAL_ACC_FLAMEQT"));
		setButtonDouble(menu, 51, "ritual.accepted.flame-speed", value -> Math.max(0.1, Math.min(5, value)), 0.1, getStrings("CFG_RITUAL_ACC_FLAMESPEED"));
		setButtonBool(menu, 52, "ritual.accepted.flame-soul", getStrings("CFG_RITUAL_ACC_FLAMESOUL"));
		
		return menu;
	}
	
	/**
	 * @return Le {@link SGMenu} des paramètres du MaceLimiter et AutoUpdater
	 */
	public static SGMenu getOtherMenu() {
		SGMenu menu = KamoofPlugin.getSpiGUI().create("§a§l" + Lang.get("CFG_TITLE") + ": §7§l" + Lang.get("CFG_TITLE_OTHER"), 3);
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
		
		setButtonBool(menu, 10, "macelimiter.enabled", getStrings("CFG_MACELIMIT_ENABLED"));
		setButtonInt(menu, 11, "macelimiter.limit", value -> Math.max(0, value), 1, getStrings("CFG_MACELIMIT_LIMIT"));
		setButtonBool(menu, 12, "macelimiter.no-enderchest", getStrings("CFG_MACELIMIT_EC"));
		
		setButtonBool(menu, 15, "autoupdate.fetch", getStrings("CFG_AUTOUPDATE_FETCH"));
		setButtonBool(menu, 16, "autoupdate.download", getStrings("CFG_AUTOUPDATE_DOWNLOAD"));
		
		return menu;
	}
	
	/**
	 * Ajoute un bouton pour retourner au menu principal.
	 * @param menu Le menu à modifier
	 * @param slot Le slot où placer l'item
	 */
	private static void setButtonMainMenu(SGMenu menu, int slot) {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§7" + Lang.get("CFG_GO_BACK"));
		item.setItemMeta(meta);
		menu.setButton(slot, new SGButton(item).withListener(event -> event.getWhoClicked().openInventory(getMainMenu().getInventory())));
	}
	
	/**
	 * Ajoute un bouton pour les notes (menu principal).
	 * @param menu Le menu à modifier (menu principal)
	 * @param slot Le slot où placer l'item
	 * @param key La clé de langue
	 */
	private static void setButtonNote(SGMenu menu, int slot, String key) {
		ItemStack item = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§a§l" + Lang.get("CFG_NOTE"));
		
		ArrayList<String> lore = new ArrayList<>();
		for (String part : Lang.get(key).replace("§r", "§a").split("\\|")) {
			String[] partSplit = part.split("\\s");
			int i = 0;
			while (i < partSplit.length) {
				StringBuilder line = new StringBuilder();
				while (line.length() < 40 && i < partSplit.length) {
					line.append(partSplit[i]).append(" ");
					i++;
				}
				lore.add("§a" + line);
			}
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		menu.setButton(slot, new SGButton(item));
	}
	
	/**
	 * Ajoute un bouton pour un booléen.
	 * @param menu Le menu à modifier
	 * @param slot Le slot où placer l'item
	 * @param path La clé du paramètre
	 * @param strings Les valeurs de langue
	 */
	private static void setButtonBool(SGMenu menu, int slot, String path, Duo<String, ArrayList<String>> strings) {
		boolean enabled = config().getBoolean(path);
		ItemStack item = new ItemStack(enabled ? Material.LIME_WOOL : Material.RED_WOOL);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName((enabled ? "§a" : "§c") + "§l" + strings.getA());
		meta.setLore(strings.getB());
		item.setItemMeta(meta);
		menu.setButton(slot, new SGButton(item).withListener(event -> {
			config().set(path, !enabled);
			getInstance().saveConfig();
			FeatureManager.refresh();
			setButtonBool(menu, slot, path, strings);
			menu.refreshInventory(event.getWhoClicked());
		}));
	}
	
	/**
	 * Ajoute un bouton pour un nombre entier.
	 * @param menu Le menu à modifier
	 * @param slot Le slot où placer l'item
	 * @param path La clé du paramètre
	 * @param validate La modification (si nécéssaire) à faire de la nouvelle valeur
	 * @param incr L'incrémentation de la valeur par clic
	 * @param strings Les valeurs de langue
	 */
	private static void setButtonInt(SGMenu menu, int slot, String path, Function<Integer, Integer> validate, int incr, Duo<String, ArrayList<String>> strings) {
		int value = config().getInt(path);
		ItemStack item = new ItemStack(Material.YELLOW_WOOL, Math.max(1, Math.min(64, (int) Math.floor((double) value / incr))));
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§e§l" + strings.getA() + ": §6§l" + value);
		meta.setLore(strings.getB());
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
			setButtonInt(menu, slot, path, validate, incr, strings);
			menu.refreshInventory(event.getWhoClicked());
		}));
	}
	
	/**
	 * Ajoute un bouton pour un nombre décimal.
	 * @param menu Le menu à modifier
	 * @param slot Le slot où placer l'item
	 * @param path La clé du paramètre
	 * @param validate La modification (si nécéssaire) à faire de la nouvelle valeur
	 * @param incr L'incrémentation de la valeur par clic
	 * @param strings Les valeurs de langue
	 */
	private static void setButtonDouble(SGMenu menu, int slot, String path, Function<Double, Double> validate, double incr, Duo<String, ArrayList<String>> strings) {
		double value = config().getDouble(path);
		ItemStack item = new ItemStack(Material.ORANGE_WOOL, Math.max(1, Math.min(64, (int) Math.floor(value / incr))));
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§e§l" + strings.getA() + ": §6§l" + value);
		meta.setLore(strings.getB());
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
			setButtonDouble(menu, slot, path, validate, incr, strings);
			menu.refreshInventory(event.getWhoClicked());
		}));
	}
	
	/**
	 * Ajoute un bouton pour une valeur parmi une liste.
	 * @param menu Le menu à modifier
	 * @param slot Le slot où placer l'item
	 * @param path La clé du paramètre
	 * @param values La liste des valeurs acceptées
	 * @param strings Les valeurs de langue
	 */
	private static void setButtonValues(SGMenu menu, int slot, String path, List<?> values, Duo<String, ArrayList<String>> strings) {
		Object value = config().get(path);
		ItemStack item = new ItemStack(Material.WHITE_WOOL);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§e§l" + strings.getA() + ": §6§l" + value);
		meta.setLore(strings.getB());
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
						if (index < 0)
							index = values.size() - 1;
					} catch (Throwable exc) {
						index = 0;
					}
				}
			}
			
			config().set(path, values.get(index));
			getInstance().saveConfig();
			setButtonValues(menu, slot, path, values, strings);
			menu.refreshInventory(event.getWhoClicked());
		}));
	}
	
	/**
	 * Ajoute un bouton pour un texte.
	 * @param menu Le menu à modifier
	 * @param slot Le slot où placer l'item
	 * @param path La clé du paramètre
	 * @param predicate La vérification de la valeur
	 * @param strings Les valeurs de langue
	 */
	private static void setButtonString(SGMenu menu, int slot, String path, Predicate<String> predicate, Duo<String, ArrayList<String>> strings) {
		String value = config().getString(path);
		ItemStack item = new ItemStack(Material.CYAN_WOOL);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§3§l" + strings.getA() + ": §6§l" + value);
		meta.setLore(strings.getB());
		item.setItemMeta(meta);
		
		menu.setButton(slot, new SGButton(item).withListener(event -> {
			if (!(event.getWhoClicked() instanceof Player player))
				return;
			new AnvilGUI.Builder().plugin(getInstance())
				.title("§3§l" + strings.getA())
				.text(value)
				.onClick((clickedSlot, stateSnapshot) -> {
					if (clickedSlot != 2)
						return List.of();
					String newValue = stateSnapshot.getText().trim();
					if (predicate.test(newValue)) {
						config().set(path, newValue);
						getInstance().saveConfig();
						setButtonString(menu, slot, path, predicate, strings);
						return List.of(AnvilGUI.ResponseAction.close());
					}
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, Message.toBaseComponent(Lang.get("CFG_INVALID_VALUE")));
					player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, SoundCategory.MASTER, 0.3f, 1.5f);
					return List.of(AnvilGUI.ResponseAction.close());
				})
				.onClose(stateSnapshot -> {
					stateSnapshot.getPlayer().openInventory(menu.getInventory());
				})
				.open(player);
		}));
	}
	
	/**
	 * Ajoute un bouton pour une couleur.
	 * @param menu Le menu à modifier
	 * @param slot Le slot où placer l'item
	 * @param path La clé du paramètre
	 * @param strings Les valeurs de langue
	 */
	private static void setButtonColor(SGMenu menu, int slot, String path, Duo<String, ArrayList<String>> strings) {
		String value = config().getString(path).toLowerCase().trim();
		Color color = ColorResolver.getColor(value);
		ItemStack item = new ItemStack(Material.CYAN_WOOL);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName("§3§l" + strings.getA() + ": "+ColorResolver.toLegacy(color)+"§l" + value);
		meta.setLore(strings.getB());
		item.setItemMeta(meta);
		
		menu.setButton(slot, new SGButton(item).withListener(event -> {
			if (!(event.getWhoClicked() instanceof Player player))
				return;
			new AnvilGUI.Builder().plugin(getInstance())
				.title("§3§l" + strings.getA())
				.text(value)
				.onClick((clickedSlot, stateSnapshot) -> {
					if (clickedSlot != 2)
						return List.of();
					String newValue = stateSnapshot.getText().trim();
					Color newColor = ColorResolver.getColor(newValue);
					if (newColor != null) {
						config().set(path, newValue);
						getInstance().saveConfig();
						setButtonColor(menu, slot, path, strings);
						return List.of(AnvilGUI.ResponseAction.close());
					}
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, Message.toBaseComponent(Lang.get("CFG_INVALID_VALUE")));
					player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, SoundCategory.MASTER, 0.3f, 1.5f);
					return List.of(AnvilGUI.ResponseAction.close());
				})
				.onClose(stateSnapshot -> {
					stateSnapshot.getPlayer().openInventory(menu.getInventory());
				})
				.open(player);
		}));
	}
	
	/**
	 * Récupère les valeurs de la langue choisie.
	 * <p>
	 * Est retourné en premier élément le nom du paramètre.
	 * <p>
	 * Est retourné en second élément la description du paramètre. Celle-ci est divisée en segments de plus ou moins 40 caractères.
	 * @param key La clé de langue
	 */
	private static Duo<String, ArrayList<String>> getStrings(String key) {
		ArrayList<String> lore = new ArrayList<>();
		String[] split = Lang.get(key).split("\\|", 2);
		for (String part : split[1].replace("§r", "§7").split("\\|")) {
			String[] partSplit = part.split("\\s");
			int i = 0;
			while (i < partSplit.length) {
				StringBuilder line = new StringBuilder();
				while (line.length() < 40 && i < partSplit.length) {
					line.append(partSplit[i]).append(" ");
					i++;
				}
				lore.add("§7" + line);
			}
		}
		return new Duo<>(split[0], lore);
	}
	
}
