package me.ghosty.kamoof.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;

@UtilityClass
public final class Utils {
	
	public static Location locationFrom(FileConfiguration config, String path) {
		if(!config.contains(config.getString(path + ".world"), true))
			return null;
		World world = Bukkit.getWorld(config.getString(path + ".world"));
		if(world == null)
			return null;
		int x = config.getInt(path + ".x");
		int y = config.getInt(path + ".y");
		int z = config.getInt(path + ".z");
		return new Location(world, x, y, z);
	}
	
	public static void locationTo(FileConfiguration config, String path, Location location) {
		config.set(path + ".world", location.getWorld().getName());
		config.set(path + ".x", location.getBlockX());
		config.set(path + ".y", location.getBlockY());
		config.set(path + ".z", location.getBlockZ());
	}
	
}
