package me.ghosty.kamoof.features.ritual;

import me.ghosty.kamoof.KamoofSMP;
import me.ghosty.kamoof.features.drophead.SkullManager;
import me.ghosty.kamoof.utils.Message;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.ArrayList;
import java.util.Map;

public final class RitualListener implements Listener {
	
	@EventHandler
	public void onPlaceHead(PlayerArmorStandManipulateEvent event) {
		Player player = event.getPlayer();
		ArmorStand entity = event.getRightClicked();
		boolean contains = false;
		for (ArmorStand stand : RitualHandler.armorStands) {
			if (entity.getEntityId() == stand.getEntityId()) {
				contains = true;
				break;
			}
		}
		if (!contains)
			return;
		if (event.getPlayerItem().getType() != Material.PLAYER_HEAD) {
			if (entity.getEquipment() != null && entity.getEquipment().getHelmet() != null
				&& entity.getEquipment().getHelmet().getType() == Material.PLAYER_HEAD
				&& event.getPlayerItem().getType() == Material.AIR
				&& !entity.getPersistentDataContainer().getOrDefault(RitualHandler.key, PersistentDataType.BOOLEAN, false))
				return;
			event.setCancelled(true);
			return;
		}
		
		long minTime = KamoofSMP.config().getInt("ritual.min-time"),
			maxTime = KamoofSMP.config().getInt("ritual.max-time"),
			time = player.getWorld().getTime();
		if(time < minTime || time > maxTime) {
			Message.send(player, "messages.ritual-wrong-time", Map.of("player", NickAPI.getOriginalName(player)));
			event.setCancelled(true);
			return;
		}
		
		String comparable = SkullManager.getName(event.getPlayerItem());
		
		ArrayList<ArmorStand> dupes = new ArrayList<>();
		boolean canRunRitual = true;
		for (ArmorStand stand : RitualHandler.armorStands) {
			if (stand.getEntityId() == entity.getEntityId())
				continue;
			if (stand.getEquipment() == null) {
				canRunRitual = false;
				continue;
			}
			ItemStack helmet = stand.getEquipment().getHelmet();
			if (helmet == null || !helmet.hasItemMeta() || !(helmet.getType() == Material.PLAYER_HEAD)) {
				canRunRitual = false;
				continue;
			}
			String comparator = SkullManager.getName(helmet);
			
			if (comparable.equalsIgnoreCase(comparator)) {
				dupes.add(stand);
			}
		}
		if (dupes.size() >= KamoofSMP.config().getInt("ritual.dupelimit")) {
			dupes.forEach(stand -> player.spawnParticle(Particle.DUST, stand.getLocation().add(0, 1.45 + 0.25 + 0.2, 0), 4, 0, 0, 0, 0, (new Particle.DustOptions(Color.ORANGE, 3)), true));
			player.spawnParticle(Particle.DUST, entity.getLocation().add(0, 1.45 + 0.25 + 0.2, 0), 4, 0, 0, 0, 0, (new Particle.DustOptions(Color.ORANGE, 3)), true);
			canRunRitual = false;
			event.setCancelled(false);
		}
		
		if (canRunRitual) {
			RitualHandler.runAnimation(player);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String pacte = RitualHandler.getPacte(player);
		if (pacte == null)
			return;
		if (pacte.equalsIgnoreCase("1")) {
			pacte = "bloody";
			player.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(RitualHandler.healthBoostModifier);
		} else if (pacte.equalsIgnoreCase("2")) {
			pacte = "forgotten";
		} else
			return;
		Message.send(player, "messages.death-" + pacte, Map.of("player", NickAPI.getOriginalName(player)));
		RitualHandler.setPacte(player, null);
	}
	
	@EventHandler
	public void onMilk(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		Bukkit.getScheduler().runTaskLater(KamoofSMP.getInstance(), () -> {
			if (!event.getPlayer().isOnline())
				return;
			String pacte = RitualHandler.getPacte(player);
			if (pacte == null || !pacte.equalsIgnoreCase("2"))
				return;
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, KamoofSMP.config().getInt("ritual.pactes.forgotten.weakness") - 1));
		}, 1L);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String pacte = RitualHandler.getPacte(player);
		if (pacte == null || !pacte.equalsIgnoreCase("2"))
			return;
		player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, KamoofSMP.config().getInt("ritual.pactes.forgotten.weakness") - 1));
	}
	
}
