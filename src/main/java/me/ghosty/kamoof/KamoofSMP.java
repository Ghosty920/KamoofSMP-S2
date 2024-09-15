package me.ghosty.kamoof;

import lombok.Getter;
import lombok.SneakyThrows;
import me.ghosty.kamoof.commands.*;
import me.ghosty.kamoof.features.autoupdate.UpdateChecker;
import me.ghosty.kamoof.features.disguise.DisguiseListener;
import me.ghosty.kamoof.features.disguise.DisguiseRestaurer;
import me.ghosty.kamoof.features.drophead.HeadDropper;
import me.ghosty.kamoof.features.macelimiter.MaceLimiter;
import me.ghosty.kamoof.features.ritual.*;
import me.ghosty.kamoof.utils.*;
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
	private static YamlConfiguration data;
	private static File dataFile;
	
	public static FileConfiguration config() {
		return instance.getConfig();
	}
	
	@SneakyThrows
	public static void saveData() {
		if(data == null)
			return;
		data.save(dataFile);
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		instance = this;
		saveDefaultConfig();
		PluginManager pm = Bukkit.getPluginManager();
		
		Lang.init();
		
		try {
			dataFile = new File(getDataFolder() + File.separator + "data.yml");
			if (!dataFile.exists()) {
				dataFile.getParentFile().mkdirs();
				dataFile.createNewFile();
			}
			data = YamlConfiguration.loadConfiguration(dataFile);
		} catch (Throwable exc) {
			exc.printStackTrace();
			log(Lang.DATA_FILE_FAILED.get());
		}
		
		pm.registerEvents(new DisguiseListener(), this);
		
		if(getConfig().getBoolean("ritual.enabled")) {
			RitualHandler.load();
			pm.registerEvents(new RitualSetup(), this);
			pm.registerEvents(new RitualListener(), this);
		}
		if(getConfig().getBoolean("autoupdate.fetch"))
			pm.registerEvents(new UpdateChecker(), this);
		if(getConfig().getBoolean("restaure.enabled"))
			pm.registerEvents(new DisguiseRestaurer(), this);
		if(getConfig().getBoolean("macelimiter.enabled"))
			pm.registerEvents(new MaceLimiter(), this);
		
		pm.registerEvents(new HeadDropper(), this);
		
		registerCommand("kamoofsmp", new KamoofCMD());
		registerCommand("givehead", new GiveHeadCMD());
		registerCommand("undisguise", new UndisguiseCMD());
		
		DisguiseRestaurer.onEnable();
		
		if(getConfig().getBoolean("metrics"))
			new Metrics(this, 23302);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		DisguiseRestaurer.onDisable();
	}
	
	private void registerCommand(String name, CommandExecutor cmd) {
		getCommand(name).setExecutor(cmd);
		if (cmd instanceof TabCompleter tc)
			getCommand(name).setTabCompleter(tc);
	}
	
	public static void log(String msg, Object... args) {
		Bukkit.getConsoleSender().sendMessage(String.format(msg, args));
	}
	
}
