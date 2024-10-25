package cc.ghosty.kamoof.features.ritual;

import cc.ghosty.kamoof.KamoofSMP;
import cc.ghosty.kamoof.utils.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.joml.Vector2d;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.*;

import static cc.ghosty.kamoof.KamoofSMP.*;

public final class RitualHandler {
	
	public static final ArrayList<ArmorStand> armorStands = new ArrayList<>();
	public static final List<Vector2d> offsets = Arrays.asList(
		new Vector2d(0, -6),
		new Vector2d(4, -4),
		new Vector2d(6, 0),
		new Vector2d(4, 4),
		new Vector2d(0, 6),
		new Vector2d(-4, 4),
		new Vector2d(-6, 0),
		new Vector2d(-4, -4)
	);
	public static final NamespacedKey key = new NamespacedKey("kamoofsmp", "ritualstand");
	static final AttributeModifier healthBoostModifier = new AttributeModifier(new NamespacedKey("kamoofsmp", "pacte"), KamoofSMP.config().getInt("ritual.pactes.bloody.hpboost"), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);
	public static boolean setup = false;
	public static Location location;
	
	public static void load() {
		setup = false;
		location = data().getLocation("ritual.data.location");
		if (location == null)
			return;
		for (int x = -1; x <= 1; x++)
			for (int z = -1; z <= 1; z++)
				location.getWorld().loadChunk(location.getBlockX() / 16 + x, location.getBlockZ() / 16 + z);
		
		armorStands.clear();
		for (ArmorStand entity : location.getWorld().getEntitiesByClass(ArmorStand.class)) {
			if (entity.getPersistentDataContainer().has(key))
				armorStands.add(entity);
		}
		if (armorStands.size() < 9) {
			armorStands.forEach(Entity::remove);
			return;
		}
		setup = true;
	}
	
	public static void runAnimation(Player player) {
		for (ArmorStand entity : armorStands) {
			entity.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
		}
		Bukkit.getScheduler().runTaskLater(KamoofSMP.getInstance(), (task) -> {
			for (ArmorStand entity : armorStands) {
				// could happen fsr
				if (entity.getEquipment() == null || entity.getEquipment().getHelmet() == null)
					return;
			}
			RitualAnimation.execute(location);
		}, 5L);
	}
	
	public static void setRitual(Location loc, Player player) {
		armorStands.forEach(Entity::remove);
		armorStands.clear();
		location = loc;
		
		ArrayList<Integer> entities = new ArrayList<>();
		float angle = -45;
		for (Vector2d offset : RitualHandler.offsets) {
			double x = loc.getBlockX() + offset.x + 0.5,
				y = loc.getBlockY() - 1.45,
				z = loc.getBlockZ() + offset.y + 0.5;
			player.spawnParticle(Particle.DUST, x, loc.getBlockY() + 0.5, z, 4, 0, 0, 0, 0, (new Particle.DustOptions(Color.YELLOW, 3)), true);
			
			Location location = new Location(player.getWorld(), x, y, z, angle += 45, 0);
			ArmorStand entity = makeArmorStand(location);
			entities.add(entity.getEntityId());
			armorStands.add(entity);
		}
		Location location = new Location(player.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY() - 1.45, loc.getBlockZ() + 0.5);
		ArmorStand entity = makeArmorStand(location);
		entities.add(entity.getEntityId());
		armorStands.add(entity);
		
		Lang.NEW_RITUAL_LOCATION.send(player, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		data().set("ritual.data.location", loc.add(0.5, 0.5, 0.5));
		saveData();
	}
	
	private static ArmorStand makeArmorStand(Location location) {
		ArmorStand entity = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		entity.setArms(false);
		entity.setBasePlate(false);
		entity.setVisible(false);
		entity.setCollidable(false);
		entity.setInvulnerable(true);
		entity.setGravity(false);
		entity.setAI(false);
		entity.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, false);
		return entity;
	}
	
	public static UUID addNewUUID() {
		UUID uuid = UUID.randomUUID();
		List<String> list = data().getStringList("pactes");
		list.add(uuid.toString());
		data().set("pactes", list);
		KamoofSMP.saveData();
		return uuid;
	}
	
	public static boolean isValidUUID(UUID uuid) {
		List<String> list = data().getStringList("pactes");
		if (!list.contains(uuid.toString()))
			return false;
		list.remove(uuid.toString());
		data().set("pactes", list);
		KamoofSMP.saveData();
		return true;
	}
	
	public static void setPacte(Player player, String pacte) {
		data().set("pacte." + player.getUniqueId(), pacte);
		KamoofSMP.saveData();
		if (pacte == null)
			return;
		switch (pacte) {
			case "1" -> {
				player.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(healthBoostModifier);
				Message.send(player, "messages.chose-bloody", Map.of("player", NickAPI.getOriginalName(player)));
			}
			case "2" -> {
				player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, KamoofSMP.config().getInt("ritual.pactes.forgotten.weakness") - 1));
				Message.send(player, "messages.chose-forgotten", Map.of("player", NickAPI.getOriginalName(player)));
			}
		}
		
		/*
		 * Animation
		 */
		final Particle lavaParticle = config().getBoolean("ritual.accepted.lava-sound")
			? Particle.DRIPPING_DRIPSTONE_LAVA
			: Particle.DRIPPING_LAVA;
		final World world = player.getWorld();
		final double radius = config().getDouble("ritual.accepted.lava-radius"),
			particles = config().getDouble("ritual.accepted.lava-particles");
		final Location loc = new SLocation(player.getLocation()).plus(0, 0.9, 0);
		
		for (int i = 0; i < particles; i++) {
			double rand1 = Math.random(), rand2 = Math.random();
			
			double phi = 2 * Math.PI * rand1;
			double posY = loc.getY() + radius * Math.cos(phi);
			double phiSin = Math.sin(phi);
			
			double angle = 2 * Math.PI * rand2;
			double posX = loc.getX() + (radius * Math.cos(angle) * phiSin);
			double posZ = loc.getZ() + (radius * Math.sin(angle) * phiSin);
			
			world.spawnParticle(lavaParticle, posX, posY, posZ, 1, 0, 0, 0, 0, null, true);
		}
		
		world.spawnParticle(
			config().getBoolean("ritual.accepted.flame-soul") ? Particle.SOUL_FIRE_FLAME : Particle.FLAME,
			loc,
			config().getInt("ritual.accepted.flame-particles"), 0, 0, 0,
			config().getDouble("ritual.accepted.flame-speed"), null, true
		);
	}
	
	public static String getPacte(Player player) {
		return data().getString("pacte." + player.getUniqueId());
	}
}
