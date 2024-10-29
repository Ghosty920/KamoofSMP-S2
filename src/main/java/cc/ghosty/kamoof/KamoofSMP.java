package cc.ghosty.kamoof;

import cc.ghosty.kamoof.commands.*;
import cc.ghosty.kamoof.features.disguise.DisguiseListener;
import cc.ghosty.kamoof.features.disguise.DisguiseRestaurer;
import cc.ghosty.kamoof.features.drophead.HeadDropper;
import cc.ghosty.kamoof.features.other.*;
import cc.ghosty.kamoof.features.ritual.*;
import cc.ghosty.kamoof.utils.*;
import com.samjakob.spigui.SpiGUI;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class KamoofSMP extends JavaPlugin {
	
	@Getter
	private static KamoofSMP instance;
	private static YamlConfiguration data;
	private static File dataFile;
	@Getter
	private static SpiGUI spiGUI;
	
	public static FileConfiguration config() {
		return instance.getConfig();
	}
	
	public static YamlConfiguration data() {
		return data;
	}
	
	@SneakyThrows
	public static void saveData() {
		if (data == null)
			return;
		data.save(dataFile);
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
	
	public static void log(String msg, Object... args) {
		Bukkit.getConsoleSender().spigot().sendMessage(Message.toBaseComponent(String.format(msg, args)));
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		instance = this;
		saveDefaultConfig();
		PluginManager pm = Bukkit.getPluginManager();
		spiGUI = new SpiGUI(this);
		
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
			log(Lang.get("DATA_FILE_FAILED"));
		}
		
		pm.registerEvents(new DisguiseListener(), this);
		
		if (getConfig().getBoolean("ritual.enabled")) {
			RitualHandler.load();
			pm.registerEvents(new RitualSetup(), this);
			pm.registerEvents(new RitualListener(), this);
		}
		if (getConfig().getBoolean("autoupdate.fetch"))
			pm.registerEvents(new UpdateChecker(), this);
		if (getConfig().getBoolean("macelimiter.enabled"))
			pm.registerEvents(new MaceLimiter(), this);
		
		if (getConfig().getBoolean("restaure.enabled")) {
			pm.registerEvents(new DisguiseRestaurer(), this);
		} else {
			data().set("restaurer", null);
			saveData();
		}
		
		pm.registerEvents(new HeadDropper(), this);
		
		registerCommand("kamoofsmp", new KamoofCMD());
		registerCommand("givehead", new GiveHeadCMD());
		registerCommand("undisguise", new UndisguiseCMD());
		
		DisguiseRestaurer.onEnable();
		pm.registerEvents(new JoinMessages(), this);
		
		// https://bstats.org/plugin/bukkit/KamoofSMP/23302
		if (getConfig().getBoolean("metrics"))
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
	
}
