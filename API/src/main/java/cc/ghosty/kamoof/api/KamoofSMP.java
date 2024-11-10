package cc.ghosty.kamoof.api;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * L'API du KamoofSMP, utilisée pour intéragir avec le plugin.
 * @since 2.0
 * @author Ghosty
 */
public abstract class KamoofSMP extends JavaPlugin {
	
	private static KamoofSMP instance;
	
	public static KamoofSMP getInstance() {
		if(instance == null)
			throw new KamoofNotInitializedException();
		return instance;
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
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
