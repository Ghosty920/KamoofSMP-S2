package me.ghosty.kamoof.features.ritual;

import lombok.experimental.UtilityClass;
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
		meta.setItemName("§4Pacte Démonique");
		meta.setTitle(null);
		meta.setFireResistant(true);
		meta.spigot().setPages(getPages());
		meta.getPersistentDataContainer().set(keyUuid, PersistentDataType.STRING, uuid.toString());
		item.setItemMeta(meta);
		return item;
	}
	
	public static UUID getUUID(ItemStack item) {
		if(item == null || !item.hasItemMeta())
			return null;
		ItemMeta meta = item.getItemMeta();
		String uuid = meta.getPersistentDataContainer().get(keyUuid, PersistentDataType.STRING);
		if(uuid == null)
			return null;
		return UUID.fromString(uuid);
	}
	
	private static List<BaseComponent[]> getPages() {
		List<String> pages = Placeholder.apply(Arrays.asList(
			"<dark_red>Pacte Ensanglanté<br><br><red>Ce pacte te permet d'augmenter ta vie de <dark_red>5 <red>coeurs t'est proposé, mais ta prochaine mort te coûtera 3 têtes...<br><dark_red><bold>Vas-tu l'accepter ?<br><br><click:run_command:%command1%><red><bold>[Accepter le Pacte]",
			"<dark_gray>Pacte Oublié<br><br><gray>Ce pacte te permet de ne <dark_gray><bold>pas laisser de tête</bold> <gray>derrière toi à ta mort, cependant durant le restant de cette vie tu seras plus faible<br><dark_gray><bold>Vas-tu l'accepter ?<br><br><click:run_command:%command2%>[Accepter le Pacte]"
		), Map.of("command1", "/kamoofsmp pacte 1", "command2", "/kamoofsmp pacte 2"));
		ArrayList<BaseComponent[]> result = new ArrayList<>();
		for (String page : pages) {
			result.add(Message.toBaseComponent(page));
		}
		return result;
	}
	
}
