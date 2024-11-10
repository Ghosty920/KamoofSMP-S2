package cc.ghosty.kamoof.commands;

import cc.ghosty.kamoof.features.drophead.SkullManager;
import cc.ghosty.kamoof.utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * La commande <code>/givehead</code> (ou accessoirement <code>/kamoofsmp givehead</code>), permettant de se donner la tête de n'importe quel joueur.
 * @since 1.0
 */
public final class GiveHeadCMD implements CommandExecutor, TabCompleter {
	
	private static final Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]{1,16}$");
	
	/**
	 * Éxecute la commande pour le joueur donné.
	 * @param player La cible
	 * @param user Le nom de la tête
	 */
	public static void execute(Player player, String user) {
		if (!usernamePattern.matcher(user).matches()) {
			Lang.send(player, "INVALID_USERNAME", user);
			return;
		}
		
		ItemStack item = SkullManager.getSkull(user);
		if (player.getInventory().addItem(item).isEmpty())
			Lang.send(player, "HEAD_GIVEN", user);
		else
			Lang.send(player, "INVENTORY_FULL");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			Lang.send(sender, "PLAYER_ONLY");
			return true;
		}
		
		if (args.length == 0) {
			player.sendMessage(String.format("§cUsage: /%s <username>", label.toLowerCase()));
			return true;
		}
		
		execute(player, args[0]);
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 1)
			return List.of();
		
		ArrayList<String> values = new ArrayList<>();
		for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			if (player == null || player.getName() == null)
				continue;
			values.add(player.getName());
		}
		
		if (args.length == 1) {
			String toCheck = args[0].toLowerCase().trim();
			values.removeIf(player -> !player.toLowerCase().trim().contains(toCheck));
		}
		
		return values;
	}
	
}
