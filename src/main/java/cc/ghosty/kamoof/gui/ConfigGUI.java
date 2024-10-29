package cc.ghosty.kamoof.gui;

import cc.ghosty.kamoof.KamoofSMP;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ConfigGUI {
	
	@Getter
	private static SGMenu mainMenu, generalMenu, disguiseMenu, ritualMenu;
	
	static {
		setupMainMenu();
	}
	
	private static void setupMainMenu() {
		mainMenu = KamoofSMP.getSpiGUI().create("§a§lConfiguration du KamoofSMP", 3);
		/* Remplissage */
		{
			ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			ItemMeta meta = item.getItemMeta();
			meta.setHideTooltip(true);
			item.setItemMeta(meta);
			SGButton glassPaneBtn = new SGButton(item).withListener(event -> event.setResult(Event.Result.DENY));
			for (int i = 0; i < 3 * 9; i++)
				mainMenu.setButton(i, glassPaneBtn);
		}
		
		ItemStack item1 = new ItemStack(Material.REDSTONE);
		ItemMeta meta1 = item1.getItemMeta();
		meta1.addItemFlags(ItemFlag.values());
		meta1.setDisplayName("§cGénéral §f& §bMace Limiter");
		item1.setItemMeta(meta1);
		mainMenu.setButton(12, new SGButton(item1).withListener(event -> {
		
		}));
	}
	
}
