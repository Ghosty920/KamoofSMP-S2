package im.ghosty.kamoof.features;

import im.ghosty.kamoof.KamoofPlugin;
import im.ghosty.kamoof.utils.Lang;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;

/**
 * Classe s'occupant de charger les {@link Feature} et d'appeller ses différentes fonctions.
 *
 * @since 1.4
 */
@UtilityClass
public final class FeatureManager {
	
	private static final ArrayList<Feature> features = new ArrayList<>();
	
	/**
	 * Ajouter et charger une Features
	 *
	 * @param features Les Features
	 */
	public static void add(Feature... features) {
		for (Feature feature : features) {
			try {
				FeatureManager.features.add(feature);
				if (feature.isEnabled())
					feature.onEnable();
			} catch (Throwable e) {
				e.printStackTrace();
				FeatureManager.features.remove(feature);
				KamoofPlugin.log(Lang.get("FEATURE_FAILED"), feature.getClass().getSimpleName());
			}
		}
	}
	
	/**
	 * Recharger les Features
	 */
	public static void refresh() {
		for (Feature feature : features) {
			boolean shouldEnable = feature.isEnabled();
			if (shouldEnable && !feature.enabled)
				feature.onEnable();
			else if (!shouldEnable && feature.enabled)
				feature.onDisable();
		}
	}
	
	/**
	 * Désactiver toutes les Features et appelle {@link Feature#onStop()}
	 */
	public static void disable() {
		for (Feature feature : features) {
			if (feature.enabled)
				feature.onDisable();
			feature.onStop();
		}
	}
	
}
