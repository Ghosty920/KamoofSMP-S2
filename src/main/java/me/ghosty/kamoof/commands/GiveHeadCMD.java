package me.ghosty.kamoof.commands;

import me.ghosty.kamoof.features.drophead.SkullManager;
import me.ghosty.kamoof.utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class GiveHeadCMD implements CommandExecutor, TabCompleter {
	
	private static final Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]{1,16}$");
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(Lang.PLAYER_ONLY.get());
			return true;
		}
		
		if (args.length == 0) {
			player.sendMessage(String.format("§cUsage: /%s <username>", label.toLowerCase()));
			return true;
		}
		
		execute(player, args[0]);
		
		return true;
	}
	
	public static void execute(Player player, String user) {
		if (!usernamePattern.matcher(user).matches()) {
			Lang.INVALID_USERNAME.send(player, user);
			return;
		}
		
		ItemStack item = SkullManager.getSkull(user);
		if (player.getInventory().addItem(item).isEmpty())
			Lang.HEAD_GIVEN.send(player, user);
		else
			Lang.INVENTORY_FULL.send(player);
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
