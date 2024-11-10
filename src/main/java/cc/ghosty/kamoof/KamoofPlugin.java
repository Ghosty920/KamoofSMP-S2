package cc.ghosty.kamoof;

import cc.ghosty.kamoof.api.KamoofSMP;
import cc.ghosty.kamoof.commands.*;
import cc.ghosty.kamoof.features.FeatureManager;
import cc.ghosty.kamoof.features.disguise.*;
import cc.ghosty.kamoof.features.drophead.HeadDropper;
import cc.ghosty.kamoof.features.drophead.SkullManager;
import cc.ghosty.kamoof.features.other.*;
import cc.ghosty.kamoof.features.ritual.RitualListener;
import cc.ghosty.kamoof.features.ritual.RitualSetup;
import cc.ghosty.kamoof.utils.*;
import com.samjakob.spigui.SpiGUI;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.haoshoku.nick.api.NickAPI;

import java.io.File;

/**
 * L'instanciation du Plugin et de l'API du KamoofSMP.
 * Préférez l'utilisation de {@link KamoofSMP#getInstance} pour obtenir l'instance du plugin.
 */
public final class KamoofPlugin extends KamoofSMP {
	
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
	
	@Override
	public ItemStack getHead(OfflinePlayer player) {
		return SkullManager.getSkull(getName(player));
	}
	
	@Override
	public void disguise(OfflinePlayer player, String name) {
		if(player instanceof Player p) {
			if(name != null)
				DisguiseManager.disguise(p, name);
			else
				DisguiseManager.undisguise(p);
		} else {
			DisguiseRestaurer.set(player.getUniqueId(), name);
		}
	}
	
	@Override
	public String getDisguise(OfflinePlayer player) {
		if(player instanceof Player p) {
			if(!NickAPI.isNicked(p))
				return null;
			return NickAPI.getName(p);
		} else {
			return DisguiseRestaurer.get(player.getUniqueId());
		}
	}
	
	@Override
	public String getName(OfflinePlayer player) {
		return player instanceof Player p ? NickAPI.getOriginalName(p) : player.getName();
	}
	
}
