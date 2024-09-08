package me.ghosty.kamoof.features.ritual;

import org.bukkit.*;
import org.joml.Vector2d;

import java.util.Arrays;
import java.util.List;

public final class RitualHandler {
	
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
	
	public static void doStuff() {
		World world = Bukkit.getWorlds().getFirst();
		
		double StartX = 0;
		double StartY = 100;
		double StartZ = 0;
		int particleCount = 100; // Nombre de particules à générer
		
		// Création des particules
		for (int i = 0; i < particleCount; i++) {
			double ratio = (double) i / (particleCount - 1);
			double x = StartX + 0.5 + -4 * ratio;
			double y = StartY + 0.25 + 0 * ratio;
			double z = StartZ + 6.5 + -2 * ratio;
			world.spawnParticle(Particle.DUST, x, y, z, 1, 0, 0, 0, 0, (new Particle.DustOptions(Color.RED, 1)), true);
		}
	}
	
	public static void doStuff2() {
		World world = Bukkit.getWorlds().getFirst();
		
		double startX = 0.5, startY = 100, startZ = 0.5;
		int particleCount = 100;
		
		for(int offset = 0; offset < offsets.size(); offset++) {
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
	}
	
	public static double interpolate(double start, double end, float fraction) {
		return(start + ((end - start) * fraction));
	}

}
