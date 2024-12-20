package im.ghosty.kamoof.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour les inventaires.
 */
@UtilityClass
public final class InventoryUtils {
	
	/**
	 * Renvoie tous les {@link ItemStack} qui sont d'un des {@link Material} de {@code types}
	 *
	 * @param inventory L'inventaire à vérifier
	 * @param types     Les matériaux possibles
	 * @return Les {@link ItemStack} correspondants
	 */
	public static Set<ItemStack> scanInventory(@NotNull Inventory inventory, @NotNull Material... types) {
		return scanInventory(inventory, 0, types);
	}
	
	/**
	 * Renvoie tous les {@link ItemStack} qui sont d'un des {@link Material} de {@code types}, comptant également ceux dans les possibles {@link Container} dans {@code inventory}
	 *
	 * @param inventory L'inventaire à vérifier
	 * @param maxDepth   La profondeur maximale du check
	 * @param types     Les matériaux possibles
	 * @return Les {@link ItemStack} correspondants, dont ceux parmis les conteneurs
	 */
	public static Set<ItemStack> scanInventory(@NotNull Inventory inventory, @NotNull @Range(from = 0, to = Long.MAX_VALUE) int maxDepth, @NotNull Material... types) {
		Set<ItemStack> items = Arrays.stream(inventory.getStorageContents())
			.filter(item -> {
				if (item == null)
					return false;
				for (Material type : types) {
					if (item.getType() == type)
						return true;
				}
				return false;
			})
			.collect(Collectors.toSet());
		
		if (maxDepth > 0) {
			Arrays.stream(inventory.getStorageContents())
				.filter(Objects::nonNull).filter(ItemStack::hasItemMeta).map(ItemStack::getItemMeta)
				.filter(meta -> meta instanceof BlockStateMeta bsMeta && bsMeta.hasBlockState() && bsMeta.getBlockState() instanceof Container)
				.map(meta -> (Container) (((BlockStateMeta) meta).getBlockState()))
				.map(Container::getInventory).collect(Collectors.toSet())
				.forEach(inv -> items.addAll(scanInventory(inv, maxDepth - 1, types)));
		}
		
		return items;
	}
	
	/**
	 * Renvoie tous les {@link ItemStack} qui sont d'un des {@link Material} de {@code types}, et l'item {@code item} s'il en est aussi
	 *
	 * @param item    L'item à vérifier
	 * @param maxDepth La profondeur maximale du check
	 * @param types   Les matériaux possibles
	 * @return Les {@link ItemStack} correspondants
	 */
	public static Set<ItemStack> scanItem(@NotNull ItemStack item, @NotNull @Range(from = 0, to = Long.MAX_VALUE) int maxDepth, @NotNull Material... types) {
		Set<ItemStack> items = new HashSet<>();
		
		if (Arrays.stream(types).anyMatch(type -> item.getType() == type))
			items.add(item);
		
		if (maxDepth > 0) {
			scan:
			{
				if (!item.hasItemMeta())
					break scan;
				ItemMeta meta = item.getItemMeta();
				if (!(meta instanceof BlockStateMeta bsMeta))
					break scan;
				if (!bsMeta.hasBlockState() || !(bsMeta.getBlockState() instanceof Container cont))
					break scan;
				items.addAll(scanInventory(cont.getInventory(), maxDepth - 1, types));
			}
		}
		
		return items;
	}
	
	/**
	 * Renvoie si au moins un des {@link ItemStack} est de {@link Material} l'un des {@code types}
	 *
	 * @param inventory L'inventaire à vérifier
	 * @param types     Les matériaux possibles
	 * @return Les {@link ItemStack} correspondants
	 */
	public static boolean hasInventory(@NotNull Inventory inventory, @NotNull Material... types) {
		return hasInventory(inventory, 0, types);
	}
	
	/**
	 * Renvoie si au moins un des {@link ItemStack} est de {@link Material} l'un des {@code types}, comptant également ceux dans les possibles {@link Container} dans {@code inventory}
	 *
	 * @param inventory L'inventaire à vérifier
	 * @param maxDepth   La profondeur maximale du check
	 * @param types     Les matériaux possibles
	 * @return Les {@link ItemStack} correspondants, dont ceux parmis les conteneurs
	 */
	public static boolean hasInventory(@NotNull Inventory inventory, @NotNull @Range(from = 0, to = Long.MAX_VALUE) int maxDepth, @NotNull Material... types) {
		for (ItemStack item : inventory.getStorageContents()) {
			if (item == null)
				continue;
			for (Material type : types) {
				if (item.getType() == type)
					return true;
			}
		}
		
		if (maxDepth > 0) {
			Set<Inventory> invs = Arrays.stream(inventory.getStorageContents())
				.filter(Objects::nonNull).filter(ItemStack::hasItemMeta).map(ItemStack::getItemMeta)
				.filter(meta -> meta instanceof BlockStateMeta bsMeta && bsMeta.hasBlockState() && bsMeta.getBlockState() instanceof Container)
				.map(meta -> (Container) (((BlockStateMeta) meta).getBlockState()))
				.map(Container::getInventory).collect(Collectors.toSet());
			for (Inventory inv : invs) {
				if (hasInventory(inv, maxDepth - 1, types))
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Renvoie si {@code item} est de {@link Material} l'un des {@code types}, ou un conteneur le possédant si valable
	 *
	 * @param item    L'item à vérifier
	 * @param maxDepth La profondeur maximale du check
	 * @param types   Les matériaux possibles
	 * @return Les {@link ItemStack} correspondants
	 */
	public static boolean hasItem(@NotNull ItemStack item, @NotNull @Range(from = 0, to = Long.MAX_VALUE) int maxDepth, @NotNull Material... types) {
		if (Arrays.stream(types).anyMatch(type -> item.getType() == type))
			return true;
		
		if (maxDepth > 0) {
			scan:
			{
				if (!item.hasItemMeta())
					break scan;
				ItemMeta meta = item.getItemMeta();
				if (!(meta instanceof BlockStateMeta bsMeta))
					break scan;
				if (!bsMeta.hasBlockState() || !(bsMeta.getBlockState() instanceof Container cont))
					break scan;
				if (hasInventory(cont.getInventory(), maxDepth - 1, types))
					return true;
			}
		}
		
		return false;
	}
	
}
