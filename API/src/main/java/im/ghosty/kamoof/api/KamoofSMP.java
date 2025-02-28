package im.ghosty.kamoof.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * L'API du KamoofSMP, utilisée pour intéragir avec le plugin.
 * @since 1.4
 * @author Ghosty
 */
public abstract class KamoofSMP {
	
	/**
	 * L'instance du plugin et de l'API.
	 */
	private static KamoofSMP instance;
	
	/**
	 * @return L'instance du plugin et de l'API.
	 * @throws KamoofNotInitializedException Si l'instance n'est pas encore chargée. N'oubliez pas d'ajouter le plugin en tant que dépendence au votre.
	 */
	public static KamoofSMP getInstance() {
		if(instance == null)
			throw new KamoofNotInitializedException();
		return instance;
	}
	
	/**
	 * Utilisé localement.
	 */
	protected KamoofSMP() {
		instance = this;
	}
	
	/**
	 * Donne une tête, utilisable par le plugin, qui représente l'{@link OfflinePlayer} <code>player</code>.
	 * @param player Le propriétaire de la tête
	 * @return La tête qui représente le joueur donné
	 */
	@NotNull
	public abstract ItemStack getHead(@NotNull OfflinePlayer player);
	
	/**
	 * Déguise l'{@link OfflinePlayer} <code>player</code> avec le pseudo <code>name</code>.
	 * <p>
	 * Si <code>player</code> n'est pas un {@link Player}, le déguisement sera appliqué à la connexion au serveur (si Restaurer est activé).
	 * <p>
	 * Si <code>name</code> est <code>null</code>, le déguisement déjà appliqué sera retiré (si présent).
	 * @param player La cible
	 * @param name Le déguisement, ou <code>null</code>
	 */
	public abstract void disguise(@NotNull OfflinePlayer player, @Nullable String name);
	
	/**
	 * Récupère le déguisement de l'{@link OfflinePlayer} <code>player</code>.
	 * <p>
	 * Si <code>player</code> n'est pas un {@link Player}, le déguisement sera celui appliqué dans Restaurer (si activé).
	 * @param player La cible
	 * @return Le déguisement, ou <code>null</code>
	 */
	@Nullable
	public abstract String getDisguise(@NotNull OfflinePlayer player);
	
	/**
	 * Récupère le pseudo original de l'{@link OfflinePlayer} <code>player</code>.
	 * @param player La cible
	 * @return Le pseudo original
	 */
	@Nullable
	public abstract String getName(@NotNull OfflinePlayer player);
	
}
