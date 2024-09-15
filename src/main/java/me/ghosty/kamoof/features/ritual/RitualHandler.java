package me.ghosty.kamoof.features.ritual;

import me.ghosty.kamoof.KamoofSMP;
import me.ghosty.kamoof.features.disguise.DisguiseManager;
import me.ghosty.kamoof.utils.Message;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.joml.Vector2d;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.*;

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
	static final AttributeModifier healthBoostModifier = new AttributeModifier(new NamespacedKey("kamoofsmp", "pacte"), KamoofSMP.config().getInt("ritual.pactes.bloody.hpboost"), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);
	public static boolean setup = false;
	public static Location location;
	
	public static void load() {
		setup = false;
		location = KamoofSMP.config().getLocation("ritual.data.location");
		if (location == null)
			return;
		for (int x = -1; x <= 1; x++)
			for (int z = -1; z <= 1; z++)
				location.getWorld().loadChunk(location.getBlockX() / 16 + x, location.getBlockZ() / 16 + z);
		
		armorStands.clear();
		List<Integer> entities = KamoofSMP.getData().getIntegerList("ritual.data.entities");
		System.out.println(Arrays.toString(entities.toArray()));
		for (ArmorStand entity : location.getWorld().getEntitiesByClass(ArmorStand.class)) {
			if (entities.contains(entity.getEntityId()))
				armorStands.add(entity);
			System.out.println(entity.getEntityId());
		}
	}
	
	public static void runAnimation(Player player) {
		RitualAnimation.execute(new Location(player.getWorld(), 0, 100, 0));
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
		
		System.out.println(Arrays.toString(entities.toArray()));
		
		player.sendMessage(KamoofSMP.PREFIX + String.format("§aNouveau lieu de Rituel: %s %s %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		KamoofSMP.getData().set("ritual.data.location", loc.add(0.5, 0.5, 0.5));
		KamoofSMP.getData().set("ritual.data.entities", entities);
		KamoofSMP.saveData();
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
		return entity;
	}
	
	public static UUID addNewUUID() {
		UUID uuid = UUID.randomUUID();
		List<String> list = KamoofSMP.getData().getStringList("pactes");
		list.add(uuid.toString());
		KamoofSMP.getData().set("pactes", list);
		KamoofSMP.saveData();
		return uuid;
	}
	
	public static boolean isValidUUID(UUID uuid) {
		List<String> list = KamoofSMP.getData().getStringList("pactes");
		if (!list.contains(uuid.toString()))
			return false;
		list.remove(uuid.toString());
		KamoofSMP.getData().set("pactes", list);
		KamoofSMP.saveData();
		return true;
	}
	
	public static void setPacte(Player player, String pacte) {
		KamoofSMP.getData().set("pacte." + DisguiseManager.getUUID(player).toString(), pacte);
		KamoofSMP.saveData();
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
	}
	
	public static String getPacte(Player player) {
		return KamoofSMP.getData().getString("pacte." + DisguiseManager.getUUID(player).toString());
	}
}
