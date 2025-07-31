package im.ghosty.kamoof;

import com.samjakob.spigui.SpiGUI;
import im.ghosty.kamoof.commands.*;
import im.ghosty.kamoof.features.FeatureManager;
import im.ghosty.kamoof.features.disguise.DisguiseListener;
import im.ghosty.kamoof.features.disguise.DisguiseRestaurer;
import im.ghosty.kamoof.features.drophead.HeadDropper;
import im.ghosty.kamoof.features.macelimiter.MaceLimiter;
import im.ghosty.kamoof.features.macelimiter.MaceNoEnderChest;
import im.ghosty.kamoof.features.other.JoinMessages;
import im.ghosty.kamoof.features.other.UpdateChecker;
import im.ghosty.kamoof.features.ritual.RitualListener;
import im.ghosty.kamoof.features.ritual.RitualSetup;
import im.ghosty.kamoof.utils.*;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.haoshoku.nick.NickAPI;

import java.io.File;
import java.io.IOException;

/**
 * L'instanciation du Plugin du KamoofSMP.
 */
public final class KamoofPlugin extends JavaPlugin {
	
	@Getter
	private static KamoofPlugin instance;
	private static YamlConfiguration data;
	private static File dataFile;
	@Getter
	private static SpiGUI spiGUI;
	
	private Metrics metrics;
	
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
		Message.send(Bukkit.getConsoleSender(), String.format(msg, args));
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		try {
			NickAPI.setupConfig(new File(getDataFolder(), "nickapi.yml"));
		} catch (IOException e) {
			NickAPI.setupConfig((ConfigurationSection) null);
		}
		NickAPI.setPlugin(this);
		
		instance = this;
		saveDefaultConfig();
		spiGUI = new SpiGUI(this);
		Lang.init();
		
		new KamoofAPI();
		
		try {
			dataFile = new File(getDataFolder(), "data.yml");
			if (!dataFile.exists()) {
				dataFile.getParentFile().mkdirs();
				dataFile.createNewFile();
			}
			data = YamlConfiguration.loadConfiguration(dataFile);
		} catch (Throwable exc) {
			exc.printStackTrace();
			log(Lang.get("DATA_FILE_FAILED"));
		}
		
		FeatureManager.add(
			new DisguiseRestaurer(), new DisguiseListener(),
			new RitualSetup(), new RitualListener(),
			new HeadDropper(),
			new MaceLimiter(), new MaceNoEnderChest(),
			new UpdateChecker(), new JoinMessages()
		);
		
		registerCommand("kamoofsmp", new KamoofCMD());
		registerCommand("givehead", new GiveHeadCMD());
		registerCommand("undisguise", new UndisguiseCMD());
		
		// https://bstats.org/plugin/bukkit/KamoofSMP/23302
		if (getConfig().getBoolean("metrics"))
			metrics = new Metrics(this, 23302);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		FeatureManager.disable();
		NickAPI.onDisable();
		metrics.shutdown();
	}
	
	private void registerCommand(String name, CommandExecutor cmd) {
		getCommand(name).setExecutor(cmd);
		if (cmd instanceof TabCompleter tc)
			getCommand(name).setTabCompleter(tc);
	}
	
}
