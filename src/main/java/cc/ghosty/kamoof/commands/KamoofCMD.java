package cc.ghosty.kamoof.commands;

import cc.ghosty.kamoof.KamoofSMP;
import cc.ghosty.kamoof.features.ritual.*;
import cc.ghosty.kamoof.gui.ConfigGUI;
import cc.ghosty.kamoof.utils.Lang;
import cc.ghosty.kamoof.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.*;

import static cc.ghosty.kamoof.KamoofSMP.*;

public final class KamoofCMD implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			Lang.send(sender, "PLAYER_ONLY");
			return true;
		}
		
		if (args.length == 2 && args[0].equalsIgnoreCase("pacte")) {
			if (!args[1].equalsIgnoreCase("1") && !args[1].equalsIgnoreCase("2"))
				return true;
			
			ItemStack book = player.getInventory().getItemInMainHand();
			UUID uuid;
			if ((uuid = RitualBook.getUUID(book)) == null) {
				book = player.getInventory().getItemInOffHand();
				if ((uuid = RitualBook.getUUID(book)) == null)
					return true;
			}
			
			if (RitualHandler.getPacte(player) != null) {
				Message.send(player, "messages.already-chose", Map.of("player", NickAPI.getOriginalName(player)));
				return true;
			}
			
			if (!RitualHandler.isValidUUID(uuid))
				return true;
			
			book.setAmount(0);
			RitualHandler.setPacte(player, args[1]);
			return true;
		}
		
		if (!player.hasPermission("kamoofsmp.admin")) {
			return showCredits(sender);
		} else if (args.length == 0)
			return showArgs(sender);
		
		switch (args[0].toLowerCase()) {
			case "info": {
				return showCredits(player);
			}
			case "config": {
				player.openInventory(ConfigGUI.getMainMenu().getInventory());
				return true;
			}
			case "givehead": {
				if (args.length < 2) {
					player.sendMessage(String.format("§a§l[KamoofSMP] §cUsage: /%s givehead <username>", label.toLowerCase()));
					return true;
				}
				GiveHeadCMD.execute(player, args[1]);
				return true;
			}
			case "reload": {
				KamoofSMP.getInstance().reloadConfig();
				Lang.init();
				Lang.send(player, "CONFIG_RELOADED");
				return true;
			}
			case "test": {
				RitualHandler.runAnimation(player);
				return true;
			}
			case "book": {
				player.getWorld().dropItemNaturally(player.getLocation(), RitualBook.getBook(RitualHandler.addNewUUID()));
				return true;
			}
			case "setup": {
				if (!config().getBoolean("ritual.enabled")) {
					Lang.send(player, "RITUAL_DISABLED");
					return true;
				}
				if (player.getInventory().addItem(RitualSetup.getItems()).isEmpty())
					Lang.send(player, "SETUP_GIVEN");
				else
					Lang.send(player, "INVENTORY_FULL");
				return true;
			}
			default: {
				return showArgs(player);
			}
		}
	}
	
	/**
	 * TODO: disguise & undisguise
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("kamoofsmp.admin")) {
			if (args.length <= 1) {
				return Arrays.asList("info", "config", "reload", "setup", "givehead");
			}
			if (args[0].equalsIgnoreCase("givehead")) {
				ArrayList<String> values = new ArrayList<>();
				for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
					if (player == null || player.getName() == null)
						continue;
					values.add(player.getName());
				}
				if (args.length == 2) {
					String toCheck = args[1].toLowerCase().trim();
					values.removeIf(player -> !player.toLowerCase().trim().contains(toCheck));
				}
				return values;
			}
		}
		return List.of();
	}
	
	private boolean showArgs(CommandSender sender) {
		String version = KamoofSMP.getInstance().getDescription().getVersion();
		Lang.send(sender, "MAIN_ARGUMENTS", version);
		return true;
	}
	
	private boolean showCredits(CommandSender sender) {
		String version = KamoofSMP.getInstance().getDescription().getVersion();
		Lang.send(sender, "CREDITS", version, Lang.SUPPORT);
		return true;
	}
}
