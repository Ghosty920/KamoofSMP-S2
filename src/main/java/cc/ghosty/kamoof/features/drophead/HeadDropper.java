package cc.ghosty.kamoof.features.drophead;

import cc.ghosty.kamoof.KamoofPlugin;
import cc.ghosty.kamoof.features.Feature;
import cc.ghosty.kamoof.features.ritual.RitualHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import xyz.haoshoku.nick.api.NickAPI;

/**
 * {@link Feature} pour gérer le drop de têtes à la mort d'un joueur.
 * @since 1.0
 */
public final class HeadDropper extends Feature {
	
	/**
	 * On utilise également du code du Rituel, donc on laisse activé et met la vérification + bas
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String pacte = RitualHandler.getPacte(player);
		if (pacte != null) {
			if (pacte.equalsIgnoreCase("2"))
				return;
			if (pacte.equalsIgnoreCase("1")) {
				ItemStack skull = SkullManager.getSkull(KamoofPlugin.getInstance().getName(player));
				skull.setAmount(KamoofPlugin.config().getInt("ritual.pactes.bloody.heads"));
				event.getDrops().add(skull);
				return;
			}
		}
		if (KamoofPlugin.config().getBoolean("drophead.enabled")) {
			event.getDrops().add(SkullManager.getSkull(KamoofPlugin.getInstance().getName(player)));
		}
	}
	
}
