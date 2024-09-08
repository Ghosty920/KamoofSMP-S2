package me.ghosty.kamoof;

import lombok.Getter;
import me.ghosty.kamoof.commands.KamoofCMD;
import me.ghosty.kamoof.features.disguise.DisguiseListener;
import me.ghosty.kamoof.features.ritual.RitualSetup;
import me.ghosty.kamoof.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class KamoofSMP extends JavaPlugin {
	
	public static final String PREFIX = "§a§l[KamoofSMP] §r";
	
	@Getter
	private static KamoofSMP instance;
	@Getter
	private static YamlConfiguration dataConfig;
	
	public static FileConfiguration config() {
		return instance.getConfig();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		instance = this;
		saveDefaultConfig();
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new DisguiseListener(), this);
		
		pm.registerEvents(new RitualSetup(), this);
		
		try {
			File file = new File(getDataFolder() + File.separator + "data.yml");
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			dataConfig = YamlConfiguration.loadConfiguration(file);
		} catch (Throwable exc) {
			exc.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("§cImpossible de charger le fichier data.\n§cSupport: https://discord.gg/akgp49Q76M");
		}
		
		registerCommand("kamoofsmp", new KamoofCMD());
		
		new Metrics(this, 23302);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	private void registerCommand(String name, CommandExecutor cmd) {
		getCommand(name).setExecutor(cmd);
		if (cmd instanceof TabCompleter tc)
			getCommand(name).setTabCompleter(tc);
	}
	
}
