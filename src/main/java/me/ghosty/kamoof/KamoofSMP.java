package me.ghosty.kamoof;

import me.ghosty.kamoof.nick.NickManager;
import me.ghosty.kamoof.utils.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class KamoofSMP extends JavaPlugin {
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		NickManager.init();
		
		new Metrics(this, 23302);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
}
