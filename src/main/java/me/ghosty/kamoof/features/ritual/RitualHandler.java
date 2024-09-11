package me.ghosty.kamoof.features.ritual;

import me.ghosty.kamoof.KamoofSMP;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.joml.Vector2d;

import java.util.*;

public final class RitualHandler {
	
	public static final ArrayList<ArmorStand> armorStands = new ArrayList<>();
	public static final List<Vector2d> offsets = Arrays.asList(
		new Vector2d(0, 6),
		new Vector2d(-4, 4),
		new Vector2d(-6, 0),
		new Vector2d(-4, -4),
		new Vector2d(0, -6),
		new Vector2d(4, -4),
		new Vector2d(6, 0),
		new Vector2d(4, 4)
	);
	public static boolean setup = false;
	public static Location location;
	
	public static void load() {
		setup = false;
		location = KamoofSMP.config().getLocation("ritual.data.location");
		armorStands.clear();
		if (location == null)
			return;
		for (int x = -1; x <= 1; x++)
			for (int z = -1; z <= 1; z++)
				location.getWorld().loadChunk(location.getBlockX() / 16 + x, location.getBlockZ() / 16 + z);
//		location.getWorld().getNearbyEntities();
	}
	
	public static void save() {
		KamoofSMP.getInstance().reloadConfig();
//		KamoofSMP.config().set("ritual.data.entities");
	}
	
	public static void doStuff(Player player) {
//		World world = Bukkit.getWorlds().getFirst();
		World world = player.getWorld();
		
		double startX = 0.5, startY = 100, startZ = 0.5;
		int particleCount = 100;
		
		for (int offset = 0; offset < offsets.size(); offset++) {
			Vector2d a = offsets.get(offset), b = offsets.get((offset + 1) % offsets.size());
			double aX = a.x + startX, aZ = a.y + startZ;
			double bX = b.x + startX, bZ = b.y + startZ;
			
			for (int i = 0; i < particleCount; i++) {
				float ratio = (float) i / (particleCount - 1);
				double x = interpolate(aX, bX, ratio);
				double y = startY + 0.25;
				double z = interpolate(aZ, bZ, ratio);
				
				world.spawnParticle(Particle.DUST, x, y, z, 1, 0, 0, 0, 0, (new Particle.DustOptions(Color.RED, 1)), true);
			}
		}
		
		Bukkit.getScheduler().runTaskTimer(KamoofSMP.getInstance(), (task) -> {
			world.setTime(Math.round((float) (world.getTime() + 1000) / 1000) * 1000L);
			if (world.getTime() == 18000)
				task.cancel();
		}, 100L, 5L);
	}
	
	public static double interpolate(double start, double end, float fraction) {
		return (start + ((end - start) * fraction));
	}
	
}
