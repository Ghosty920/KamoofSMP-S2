package me.ghosty.kamoof.features.ritual;

import lombok.SneakyThrows;
import me.ghosty.kamoof.KamoofSMP;
import me.ghosty.kamoof.utils.*;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.scheduler.BukkitTask;
import org.joml.Vector2d;

import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

import static me.ghosty.kamoof.KamoofSMP.config;
import static me.ghosty.kamoof.features.ritual.RitualHandler.offsets;
import static me.ghosty.kamoof.utils.Utils.interpolate;

public final class RitualAnimation {
	
	private static final ArrayList<Runnable> particles = new ArrayList<>();
	private static boolean stopped = false;
	
	@SneakyThrows
	public static void execute(Location location) {
		final World world = location.getWorld();
		final double startX = location.getBlockX() + 0.5, startY = location.getBlockY() + 0.25, startZ = location.getBlockZ() + 0.5;
		final double height = 8, endY = startY + height;
		final SLocation centeredLoc = new SLocation(location.getWorld(), startX, location.getBlockY() + height / 2, startZ);
		
		final Particle.DustOptions dust = new Particle.DustOptions(
			ColorResolver.getColor(config().getString("ritual.animation.color")),
			(float) config().getDouble("ritual.animation.size")
		);
		
		stopped = false;
		particles.clear();
		
		var ref = new Object() {
			int currentOffset = 0;
		};
		
		// Modification des têtes
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		PlayerProfile ownerProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
		PlayerTextures textures = ownerProfile.getTextures();
		textures.setSkin(new URL("http://textures.minecraft.net/texture/f8912bc1ad3ddbe39a19b734a42d8548964bb0a9ce58a52f1a6ae37121524"));
		ownerProfile.setTextures(textures);
		meta.setOwnerProfile(ownerProfile);
		skull.setItemMeta(meta);
		for (ArmorStand entity : RitualHandler.armorStands) {
			entity.getPersistentDataContainer().set(RitualHandler.key, PersistentDataType.BOOLEAN, true);
			entity.getEquipment().setHelmet(skull, true);
		}
		
		// Modification du temps
		boolean hasDayCycle = world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE);
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		final long previousWorldTime = world.getTime();
		final long timeIncr = config().getLong("ritual.animation.time-incr");
		Bukkit.getScheduler().runTaskTimer(KamoofSMP.getInstance(), (task) -> {
			world.setTime(Math.round((float) (world.getTime() + timeIncr) / timeIncr) * timeIncr);
			if (world.getTime() == 18000) {
				task.cancel();
			}
		}, 0L, 3L);
		
		Consumer<Double> drawLargeCircles = (y) -> {
			drawCircle(world, startX, y, startZ, 4.25, 100, dust);
			drawCircle(world, startX, y, startZ, 4.5, 100, dust);
			drawCircle(world, startX, y, startZ, 7.25, 150, dust);
		};
		
		Consumer<BukkitTask> part4 = (task) -> {
			if (stopped) {
				task.cancel();
				return;
			}
			
			double radius = config().getDouble("ritual.animation.sphere.radius");
			spawnSphere(world, startX, startY + height / 2, startZ, radius);
			
			Bukkit.getScheduler().runTaskLater(KamoofSMP.getInstance(), () -> {
				stopped = true;
				world.setTime(previousWorldTime);
				world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, hasDayCycle);
				Bukkit.spigot().broadcast(Message.toBaseComponent(config().getString("messages.ritualdone")));
				world.dropItemNaturally(centeredLoc, RitualBook.getBook(RitualHandler.addNewUUID()));
				world.strikeLightning(centeredLoc.plus(0, radius, 0));
				
				for (ArmorStand entity : RitualHandler.armorStands) {
					entity.getPersistentDataContainer().set(RitualHandler.key, PersistentDataType.BOOLEAN, false);
					entity.getEquipment().setHelmet(null, true);
				}
			}, 200L);
		};
		
		Consumer<BukkitTask> part3 = (task) -> {
			if (stopped) {
				task.cancel();
				return;
			}
			
			particles.add(() -> world.spawnParticle(Particle.FLAME, startX, startY + height / 2, startZ, 2, 5, 5, 5, 2, null, true));
			world.playSound(centeredLoc, Sound.BLOCK_ANVIL_PLACE, SoundCategory.AMBIENT, 0.5f, 0.5f);
			
			drawLargeCircles.accept(startY);
			
			Bukkit.getScheduler().runTaskLater(KamoofSMP.getInstance(), (task2) -> {
				if (stopped) {
					task2.cancel();
					return;
				}
				
				drawLargeCircles.accept(endY);
				
				for (int i = 0; i < offsets.size(); i++) {
					Vector2d a = offsets.get(i), b = offsets.get((i + 1) % offsets.size());
					double aX = a.x + startX, aZ = a.y + startZ;
					double bX = b.x + startX, bZ = b.y + startZ;
					
					drawCircle(world, aX, endY, aZ, 1.5, 40, dust);
					drawLine(world, endY, aX, aZ, bX, bZ, dust);
				}
				
				var ref2 = new Object() {
					int i = 0;
				};
				SLocation lightningLoc = centeredLoc.plus(0, height * 1.5, 0);
				Bukkit.getScheduler().runTaskTimer(KamoofSMP.getInstance(), (task3) -> {
					if (ref2.i >= 25) {
						task3.cancel();
						return;
					}
					ref2.i++;
					
					world.strikeLightning(lightningLoc);
//					world.strikeLightning(lightningLoc);
					
				}, 15L, 2L);
				
				Bukkit.getScheduler().runTaskLater(KamoofSMP.getInstance(), part4, 60L);
			}, 20L);
		};
		
		Consumer<BukkitTask> part2 = (task) -> {
			if (stopped) {
				task.cancel();
				return;
			}
			
			for (Vector2d offset : offsets) {
				double x = startX + offset.x, z = startZ + offset.y;
				for (int i = 0; i <= 30; i++) {
					float ratio = (float) i / 30;
					double y = interpolate(startY, endY, ratio);
					
					particles.add(() -> world.spawnParticle(Particle.DUST, x, y, z, 1, 0, 0, 0, 0, dust));
				}
			}
			
			Bukkit.getScheduler().runTaskTimer(KamoofSMP.getInstance(), (task2) -> {
				if (stopped) {
					task2.cancel();
					return;
				}
				
				Vector2d offset = offsets.get(ref.currentOffset);
				ref.currentOffset++;
				drawCircle(world, startX + offset.x, startY, startZ + offset.y, 1.5, 40, dust);
				
				if (ref.currentOffset >= offsets.size()) {
					task2.cancel();
					ref.currentOffset = 0;
					Bukkit.getScheduler().runTaskLater(KamoofSMP.getInstance(), part3, 10L);
				}
			}, 10L, 10L);
		};
		
		Consumer<BukkitTask> part1 = (task) -> {
			if (stopped) {
				task.cancel();
				return;
			}
			
			Vector2d a = offsets.get(ref.currentOffset), b = offsets.get((ref.currentOffset + 1) % offsets.size());
			ref.currentOffset++;
			
			double aX = a.x + startX, aZ = a.y + startZ;
			double bX = b.x + startX, bZ = b.y + startZ;
			
			drawLine(world, startY, aX, aZ, bX, bZ, dust);
			
			if (ref.currentOffset >= offsets.size()) {
				task.cancel();
				ref.currentOffset = 0;
				
				world.playSound(centeredLoc, Sound.BLOCK_ANVIL_USE, SoundCategory.AMBIENT, 1, 0.3f);
				
				Bukkit.getScheduler().runTaskLater(KamoofSMP.getInstance(), part2, 40L);
			}
		};
		
		Bukkit.getScheduler().runTaskTimer(KamoofSMP.getInstance(), part1, 15L, 20L);
		
		Bukkit.getScheduler().runTaskTimer(KamoofSMP.getInstance(), (task) -> {
			if (stopped) {
				task.cancel();
				return;
			}
			particles.forEach(Runnable::run);
		}, 0L, 2L);
	}
	
	/**
	 * Fais une ligne de particules
	 *
	 * @param world Le monde
	 * @param y     Position Y
	 * @param aX    Position X du point A
	 * @param aZ    Position Z du point A
	 * @param bX    Position X du point B
	 * @param bZ    Position Y du point B
	 */
	private static void drawLine(World world, double y, double aX, double aZ, double bX, double bZ, Particle.DustOptions dust) {
		for (int i = 0; i < 20; i++) {
			float ratio = (float) i / (20 - 1);
			double x = interpolate(aX, bX, ratio);
			double z = interpolate(aZ, bZ, ratio);
			
			particles.add(() -> world.spawnParticle(Particle.DUST, x, y, z, 1, 0, 0, 0, 0, dust, true));
		}
	}
	
	/**
	 * Fais un cercle de particules
	 *
	 * @param world          Le monde
	 * @param x              Position X
	 * @param y              Position Y
	 * @param z              Position Z
	 * @param radius         Le rayon du cercle
	 * @param particlesCount Le nombre de particules
	 */
	private static void drawCircle(World world, double x, double y, double z, double radius, int particlesCount, Particle.DustOptions dust) {
		for (int i = 0; i < particlesCount; i++) {
			double angle = 2 * Math.PI * i / particlesCount;
			double posX = x + (radius * Math.cos(angle));
			double posZ = z + (radius * Math.sin(angle));
			
			particles.add(() -> world.spawnParticle(Particle.DUST, posX, y, posZ, 1, 0, 0, 0, 0, dust, true));
		}
	}
	
	/**
	 * Fais une sphère de particules
	 *
	 * @param world Le monde
	 * @param x     Position X
	 * @param y     Position Y
	 * @param z     Position Z
	 */
	private static void spawnSphere(World world, double x, double y, double z, double radius) {
		final int particles = config().getInt("ritual.animation.sphere.particles");
		final double lavaChance = config().getDouble("ritual.animation.sphere.lava-chance") / 100;
		final Particle lavaParticle = config().getBoolean("ritual.animation.sphere.lava-sound")
			? Particle.DRIPPING_DRIPSTONE_LAVA
			: Particle.DRIPPING_LAVA;
		final Particle.DustOptions dust = new Particle.DustOptions(
			ColorResolver.getColor(config().getString("ritual.animation.sphere.color")),
			(float) config().getDouble("ritual.animation.sphere.size")
		);
		
		Bukkit.getScheduler().runTaskTimer(KamoofSMP.getInstance(), (task) -> {
			if (stopped) {
				task.cancel();
				return;
			}
			
			for (int i = 0; i < particles; i++) {
				
				double rand1 = Math.random(), rand2 = Math.random();
				
				double phi = 2 * Math.PI * rand1;
				double posY = y + radius * Math.cos(phi);
				double phiSin = Math.sin(phi);
				
				double angle = 2 * Math.PI * rand2;
				double posX = x + (radius * Math.cos(angle) * phiSin);
				double posZ = z + (radius * Math.sin(angle) * phiSin);
				
				if (Math.random() > lavaChance)
					world.spawnParticle(Particle.DUST, posX, posY, posZ, 1, 0, 0, 0, 0, dust, true);
				else
					world.spawnParticle(lavaParticle, posX, posY, posZ, 1, 0, 0, 0, 0, null, true);
				
			}
		}, 0L, 5L);
		
	}
	
}
