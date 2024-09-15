package me.ghosty.kamoof.commands;

import me.ghosty.kamoof.features.disguise.DisguiseManager;
import me.ghosty.kamoof.utils.Lang;
import me.ghosty.kamoof.utils.Message;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.List;
import java.util.Map;

public final class UndisguiseCMD implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(Lang.PLAYER_ONLY.get());
			return true;
		}
		
		if (NickAPI.isNicked(player)) {
			String disguise = NickAPI.getName(player);
			DisguiseManager.undisguise(player);
			Message.send(player, "messages.undisguise", Map.of("player", NickAPI.getOriginalName(player), "nick", disguise));
		} else {
			Message.send(player, "messages.nodisguise", Map.of("player", NickAPI.getOriginalName(player)));
		}
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return List.of();
	}
	
}
