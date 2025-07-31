package im.ghosty.kamoof.api;

import org.bukkit.plugin.Plugin;

/**
 * Exception quand l'instance du KamoofSMP est appellé mais n'est pas encore chargée.
 * <p>
 * Ceci peut-être dû à l'absence de la dépendance dans le <code>plugin.yml</code>, ou à un appel de {@link KamoofSMP#getInstance} avant le {@link Plugin#onEnable}
 * @since 1.4
 * @author Ghosty
 */
public final class KamoofNotInitializedException extends RuntimeException {
	
	KamoofNotInitializedException() {
		super("The KamoofSMP instance is not set yet. Please add it as a dependency and don't call it before Plugin#onEnable.");
	}
	
}
