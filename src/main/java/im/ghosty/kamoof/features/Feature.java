package im.ghosty.kamoof.features;

import im.ghosty.kamoof.KamoofPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Une Feature est un composant essentiel du KamoofSMP. Ils sont disponibles dans les différents sous-packages.
 * @since 1.4
 */
public abstract class Feature implements Listener {
	
	protected boolean enabled;
	
	/**
	 * Permet de savoir si la Feature est activée.
	 * <p>
	 * Cette fonction devrait faire un call à {@link org.bukkit.configuration.file.FileConfiguration#getBoolean(String)}
	 *
	 * @return L'état de la Feature.
	 */
	public abstract boolean isEnabled();
	
	/**
	 * Fonction appelée quand la Feature est activée, notamment:
	 * <ul>
	 *     <li>Au démarrage du serveur</li>
	 *     <li>Au reload de la config (si changement)</li>
	 *     <li>À l'activation (depuis le menu config)</li>
	 * </ul>
	 */
	public void onEnable() {
		enabled = true;
		Bukkit.getPluginManager().registerEvents(this, KamoofPlugin.getInstance());
	}
	
	/**
	 * Fonction appelée quand la Feature est désactivée, notamment:
	 * <ul>
	 *     <li>Au reload de la config (si changement)</li>
	 *     <li>À la désactivation (depuis le menu config)</li>
	 * </ul>
	 */
	public void onDisable() {
		enabled = false;
		HandlerList.unregisterAll(this);
	}
	
	/**
	 * Fonction appelée quand la serveur est fermé. Est appelée après {@link Feature#onDisable()}
	 */
	public void onStop() {
	}
	
}
