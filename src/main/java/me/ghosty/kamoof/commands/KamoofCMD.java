package me.ghosty.kamoof.commands;

import me.ghosty.kamoof.KamoofSMP;
import me.ghosty.kamoof.features.ritual.*;
import me.ghosty.kamoof.utils.Message;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

import static me.ghosty.kamoof.KamoofSMP.PREFIX;

public class KamoofCMD implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(PREFIX + "§cCommande réservée aux joueurs.");
			return true;
		}
		
		if (args.length != 0 && args[0].equalsIgnoreCase("pacte")) {
			System.out.println(Arrays.toString(args));
			return true;
		}
		
		if (!player.hasPermission("kamoofsmp.admin")) {
			return showCredits(sender);
		} else if (args.length == 0)
			return showArgs(sender);
		
		switch (args[0].toLowerCase()) {
			case "info": {
				return showCredits(sender);
			}
			case "givehead": {
				return true;
			}
			case "disguise": {
				return true;
			}
			case "undisguise": {
				return true;
			}
			case "reload": {
				KamoofSMP.getInstance().reloadConfig();
				player.sendMessage(PREFIX + "§aConfig rechargée.");
				return true;
			}
			case "test": {
				RitualHandler.doStuff();
				return true;
			}
			case "test2": {
				RitualHandler.doStuff2();
				return true;
			}
			case "book": {
				player.getWorld().dropItem(player.getLocation().add(0, 5, 0), RitualBook.getBook(UUID.randomUUID()));
				Bukkit.spigot().broadcast(Message.toBaseComponent("<dark_red>Le Pacte des têtes a été achevé. CRAIGNEZ SON POUVOIR SI VOUS L'OSEZ !!"));
				return true;
			}
			case "setup": {
				if(player.getInventory().addItem(RitualSetup.getItem()).isEmpty())
					player.sendMessage(PREFIX + "§aItem Setup donné !");
				else
					player.sendMessage(PREFIX + "§cInventaire plein !");
			}
		}
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("kamoofsmp.admin")) {
			return Arrays.asList("info", "givehead", "disguise", "undisguise", "reload", "setup");
		}
		return List.of();
	}
	
	private boolean showArgs(CommandSender sender) {
		sender.sendMessage("§2KamoofSMP Version 2.0",
			"§a§lArguments:",
			"§a- info: Des infos sur le plugin",
			"§a- givehead: (Se) Donner n'importe quelle tête",
			"§a- disguise: (Se) Déguiser avec n'importe quel pseudo",
			"§a- undisguise: Retirer son/un déguisement",
			"§a- reload: Recharger la configuration",
			"§a- setup: Placer le rituel"
		);
		return true;
	}
	
	private boolean showCredits(CommandSender sender) {
		sender.sendMessage(
			"§2KamoofSMP Version 2.0",
			"§a- Systèmes par Ghosty920",
			"§a- Rituel par Solme",
			"§aBasé sur le plugin d'Arcane pour le SohranSMP"
		);
		return true;
	}
	
}
