package cc.ghosty.kamoof;

import cc.ghosty.kamoof.commands.*;
import cc.ghosty.kamoof.features.FeatureManager;
import cc.ghosty.kamoof.features.disguise.DisguiseListener;
import cc.ghosty.kamoof.features.disguise.DisguiseRestaurer;
import cc.ghosty.kamoof.features.drophead.HeadDropper;
import cc.ghosty.kamoof.features.other.*;
import cc.ghosty.kamoof.features.ritual.RitualListener;
import cc.ghosty.kamoof.features.ritual.RitualSetup;
import cc.ghosty.kamoof.utils.*;
import com.samjakob.spigui.SpiGUI;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
		spiGUI = new SpiGUI(this);
		Lang.init();
		
		new KamoofAPI();
		
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
		
		FeatureManager.add(
			new DisguiseRestaurer(), new DisguiseListener(),
			new RitualSetup(), new RitualListener(),
			new HeadDropper(),
			new MaceLimiter(), new UpdateChecker(), new JoinMessages()
		);
		
		registerCommand("kamoofsmp", new KamoofCMD());
		registerCommand("givehead", new GiveHeadCMD());
		registerCommand("undisguise", new UndisguiseCMD());
		
		// https://bstats.org/plugin/bukkit/KamoofSMP/23302
		if (getConfig().getBoolean("metrics"))
			new Metrics(this, 23302);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		FeatureManager.disable();
	}
	
	private void registerCommand(String name, CommandExecutor cmd) {
		getCommand(name).setExecutor(cmd);
		if (cmd instanceof TabCompleter tc)
			getCommand(name).setTabCompleter(tc);
	}
	
}
