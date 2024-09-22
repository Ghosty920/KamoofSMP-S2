package me.ghosty.kamoof.features.ritual;

import lombok.experimental.UtilityClass;
import me.ghosty.kamoof.KamoofSMP;
import me.ghosty.kamoof.utils.Message;
import me.ghosty.kamoof.utils.Placeholder;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

@UtilityClass
public final class RitualBook {
	
	private static final NamespacedKey keyUuid = new NamespacedKey("kamoofsmp", "uuid");
	
	public static ItemStack getBook(UUID uuid) {
		ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) item.getItemMeta();
		meta.setGeneration(BookMeta.Generation.TATTERED);
		try {
			meta.setItemName(KamoofSMP.config().getString("ritual.name"));
		} catch (Throwable exc) {
			meta.setDisplayName(KamoofSMP.config().getString("ritual.name"));
		}
		meta.setTitle(null);
		meta.setFireResistant(true);
		meta.spigot().setPages(getPages());
		meta.getPersistentDataContainer().set(keyUuid, PersistentDataType.STRING, uuid.toString());
		item.setItemMeta(meta);
		return item;
	}
	
	public static UUID getUUID(ItemStack item) {
		if (item == null || !item.hasItemMeta())
			return null;
		ItemMeta meta = item.getItemMeta();
		String uuid = meta.getPersistentDataContainer().get(keyUuid, PersistentDataType.STRING);
		if (uuid == null)
			return null;
		return UUID.fromString(uuid);
	}
	
	private static List<BaseComponent[]> getPages() {
		List<String> pages = Placeholder.apply(KamoofSMP.config().getStringList("ritual.pages"), Map.of("command1", "/kamoofsmp pacte 1", "command2", "/kamoofsmp pacte 2"));
		ArrayList<BaseComponent[]> result = new ArrayList<>();
		for (String page : pages) {
			result.add(Message.toBaseComponent(page));
		}
		return result;
	}
	
}
