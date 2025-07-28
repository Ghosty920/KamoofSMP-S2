package im.ghosty.kamoof.utils;

import im.ghosty.kamoof.KamoofPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.EquipmentSlotGroup;

@UtilityClass
public final class CompatibilityUtils {
	
	private static final boolean IS_MINECRAFT_1_21 = Material.getMaterial("MACE") != null;
	private static final boolean DO_BUNDLES_EXIST = Material.getMaterial("BUNDLE") != null;
	private static Attribute MAX_HEALTH_ATTRIBUTE;
	private static AttributeModifier MAX_HEALTH_ATTRIBUTE_MODIFIER;
	private static InventoryAction PAPER_PLACE_INTO_BUNDLE_ACTION, PAPER_PLACE_FROM_BUNDLE_ACTION;
	
	public static boolean isMinecraft1_21() {
		// 1.21
		return IS_MINECRAFT_1_21;
	}
	
	public static boolean doBundlesExist() {
		// je sais pas vraiment si c'est utile en fait... go laisser ig
		return DO_BUNDLES_EXIST;
	}
	
	public static Attribute getMaxHealthAttribute() {
		if (MAX_HEALTH_ATTRIBUTE != null)
			return MAX_HEALTH_ATTRIBUTE;
		
		try {
			// 1.21.3
			MAX_HEALTH_ATTRIBUTE = Attribute.MAX_HEALTH;
		} catch (Throwable exc) {
			MAX_HEALTH_ATTRIBUTE = Attribute.valueOf("GENERIC_MAX_HEALTH");
		}
		return MAX_HEALTH_ATTRIBUTE;
	}
	
	public static AttributeModifier getMaxHealthAttributeModifier() {
		if (MAX_HEALTH_ATTRIBUTE_MODIFIER != null)
			return MAX_HEALTH_ATTRIBUTE_MODIFIER;
		
		int hpBoost = KamoofPlugin.config().getInt("ritual.pactes.bloody.hpboost");
		try {
			// 1.21
			MAX_HEALTH_ATTRIBUTE_MODIFIER = new AttributeModifier(new NamespacedKey("kamoofsmp", "pacte"), hpBoost, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);
		} catch (Throwable exc) {
			MAX_HEALTH_ATTRIBUTE_MODIFIER = new AttributeModifier("kamoofsmp:pacte", hpBoost, AttributeModifier.Operation.ADD_NUMBER);
		}
		return MAX_HEALTH_ATTRIBUTE_MODIFIER;
	}
	
	public static InventoryAction getPaperPlaceIntoBundleAction() {
		if(PAPER_PLACE_INTO_BUNDLE_ACTION != null)
			return PAPER_PLACE_INTO_BUNDLE_ACTION;
		
		try {
			// Ajout de Paper
			PAPER_PLACE_INTO_BUNDLE_ACTION = InventoryAction.valueOf("PLACE_ALL_INTO_BUNDLE");
		} catch (Throwable exc) {
			// Spigot utilise SWAP_WITH_CURSOR, mais ici on s'en fout un peu
			PAPER_PLACE_INTO_BUNDLE_ACTION = InventoryAction.UNKNOWN;
		}
		return PAPER_PLACE_INTO_BUNDLE_ACTION;
	}

	public static InventoryAction getPaperPlaceFromBundleAction() {
		if (PAPER_PLACE_FROM_BUNDLE_ACTION != null)
		    return PAPER_PLACE_FROM_BUNDLE_ACTION;

		try {
			// Ajout de Paper
			PAPER_PLACE_FROM_BUNDLE_ACTION = InventoryAction.valueOf("PLACE_FROM_BUNDLE");
		} catch (Throwable exc) {
			// Spigot utilise autre chose, mais ici on s'en fout un peu
			PAPER_PLACE_FROM_BUNDLE_ACTION = InventoryAction.UNKNOWN;
		}
		return PAPER_PLACE_FROM_BUNDLE_ACTION;
	}
	
}
