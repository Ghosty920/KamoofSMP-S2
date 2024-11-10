package cc.ghosty.kamoof.features;

import lombok.experimental.UtilityClass;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

/**
 * Classe s'occupant de charger les {@link Feature} et d'appeller ses différentes fonctions.
 * @since 1.4
 */
@UtilityClass
public final class FeatureManager {
	
	private static final ArrayList<Feature> features = new ArrayList<>();
	
	/**
	 * Ajouter et charger une Features
	 * @param features Les Features
	 */
	public static void add(Feature... features) {
		for (Feature feature : features) {
			FeatureManager.features.add(feature);
			if (feature.isEnabled())
				feature.onEnable();
		}
	}
	
	/**
	 * Recharger les Features
	 */
	public static void refresh() {
		for (Feature feature : features) {
			boolean shouldEnable = feature.isEnabled();
			if(shouldEnable && !feature.enabled)
				feature.onEnable();
			else if(!shouldEnable && feature.enabled)
				feature.onDisable();
		}
	}
	
	/**
	 * Désactiver toutes les Features et appelle {@link Feature#onStop()}
	 */
	public static void disable() {
		for (Feature feature : features) {
			feature.onDisable();
			feature.onStop();
		}
	}
	
}
