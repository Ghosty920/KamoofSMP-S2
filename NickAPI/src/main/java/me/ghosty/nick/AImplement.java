package me.ghosty.nick;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AImplement {
	public void nick(Player player, String toNick) {
		if (player == null || !player.isOnline()) {
			return;
		}
		NickUser user = NickHandler.getUser(player);
		if (toNick.length() >= 16) {
			toNick = toNick.substring(0, 16);
		}
		user.getQueueMap().put("SetTag", toNick);
		Reflection.setField(user.getNickProfile(), "name", toNick);
		user.setNickSet(true);
	}
	
	public void resetNick(Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		NickUser user = NickHandler.getUser(player);
		user.getQueueMap().put("ResetTag", true);
		user.setNickSet(false);
	}
	
	public boolean isNicked(Player player) {
		if (player == null || !player.isOnline()) {
			return false;
		}
		return NickHandler.getUser(player).isNickSet();
	}
	
	public boolean isSkinChanged(Player player) {
		if (player == null || !player.isOnline()) {
			return false;
		}
		return NickHandler.getUser(player).isSkinSet();
	}
	
	public void setSkin(Player player, String toSkin) {
		if (player == null || !player.isOnline()) {
			return;
		}
		NickUser user = NickHandler.getUser(player);
		user.getQueueMap().put("SetSkin", toSkin);
		user.setSkinSet(true);
	}
	
	public void setSkin(Player player, String value, String signature) {
		if (player == null || !player.isOnline()) {
			return;
		}
		NickUser user = NickHandler.getUser(player);
		user.getQueueMap().put("SetSkin", new String[]{value, signature});
		user.setSkinSet(true);
	}
	
	public void resetSkin(Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		NickUser user = NickHandler.getUser(player);
		user.getQueueMap().put("ResetSkin", true);
		user.setSkinSet(false);
	}
	
	public String getOriginalGameProfileName(Player player) {
		if (player == null || !player.isOnline()) {
			return "null";
		}
		return NickHandler.getUser(player).getOriginalName();
	}
	
	public String[] getSkinData(Player player) {
		if (player == null || !player.isOnline()) {
			return new String[]{"null", "null"};
		}
		NickUser user = NickHandler.getUser(player);
		GameProfile profile = user.getNickProfile();
		String value = "";
		String signature = "";
		for (Property property : profile.getProperties().get((Object) "textures")) {
			if (!NickPlugin.getPlugin().getHandler().getVersion().equalsIgnoreCase("v1_20_R2")) {
				value = property.getValue();
				signature = property.getSignature();
				continue;
			}
			try {
				value = (String) property.getClass().getMethod("value", new Class[0]).invoke(property, new Object[0]);
				signature = (String) property.getClass().getMethod("signature", new Class[0]).invoke(property, new Object[0]);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return new String[]{value, signature};
	}
	
	public String getGameProfileName(Player player) {
		if (player == null || !player.isOnline()) {
			return "null";
		}
		return NickHandler.getUser(player).getOriginalProfile().getName();
	}
	
	public void setGameProfileName(Player player, String name) {
		if (player == null || !player.isOnline()) {
			return;
		}
		if (name.length() > 16) {
			name = name.substring(0, 16);
		}
		if (NickAPI.getConfig().isGameProfileChanges()) {
			Reflection.setField(NickHandler.getUser(player).getOriginalProfile(), "name", name);
		}
	}
	
	public void resetGameProfileName(Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		if (NickAPI.getConfig().isGameProfileChanges()) {
			Reflection.setField(NickHandler.getUser(player).getOriginalProfile(), "name", NickAPI.getOriginalGameProfileName(player));
		}
	}
	
	public UUID getUniqueId(Player player) {
		if (player == null || !player.isOnline()) {
			return null;
		}
		return NickHandler.getUser(player).getNickProfile().getId();
	}
	
	public void setUniqueId(Player player, UUID uuid) {
		if (player == null || !player.isOnline()) {
			return;
		}
		NickHandler.getUser(player).getQueueMap().put("SetUUID", uuid);
	}
	
	public void setUniqueId(Player player, String name) {
		if (player == null || !player.isOnline()) {
			return;
		}
		NickHandler.getUser(player).getQueueMap().put("SetUUID", name);
	}
	
	public void resetUniqueId(Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		NickHandler.getUser(player).getQueueMap().put("ResetUUID", true);
	}
	
	public List<UUID> getBypassList(Player nickedPlayer) {
		if (nickedPlayer == null || !nickedPlayer.isOnline()) {
			return null;
		}
		return NickHandler.getUser(nickedPlayer).getBypassList();
	}
	
	public void addBypass(Player nickedPlayer, Player bypassPlayer) {
		if (nickedPlayer == null || !nickedPlayer.isOnline()) {
			return;
		}
		this.addBypass(nickedPlayer.getUniqueId(), bypassPlayer.getUniqueId());
	}
	
	public void addBypass(Player nickedPlayer, Player... bypassPlayers) {
		if (nickedPlayer == null || !nickedPlayer.isOnline()) {
			return;
		}
		UUID[] uuids = new UUID[bypassPlayers.length];
		int count = 0;
		for (Player player : bypassPlayers) {
			uuids[count] = player.getUniqueId();
			++count;
		}
		this.addBypass(nickedPlayer.getUniqueId(), uuids);
	}
	
	public void addBypass(UUID nickedPlayerUUID, UUID bypassPlayerUUID) {
		this.addBypass(nickedPlayerUUID, new UUID[]{bypassPlayerUUID});
	}
	
	public void addBypass(UUID nickedPlayerUUID, UUID... bypassPlayerUUIDs) {
		if (Bukkit.getPlayer((UUID) nickedPlayerUUID) == null || !Bukkit.getPlayer((UUID) nickedPlayerUUID).isOnline()) {
			return;
		}
		NickUser user = NickHandler.getUserByUUID(nickedPlayerUUID);
		UUID[] uuidArr = new UUID[bypassPlayerUUIDs.length];
		int count = 0;
		for (UUID uuid : bypassPlayerUUIDs) {
			if (user.getBypassList().contains(uuid)) continue;
			user.getBypassList().add(uuid);
			uuidArr[count] = uuid;
			++count;
		}
		user.getQueueMap().put("AddBypass", uuidArr);
	}
	
	public void removeBypass(Player nickedPlayer, Player bypassPlayer) {
		if (nickedPlayer == null || !nickedPlayer.isOnline()) {
			return;
		}
		this.removeBypass(nickedPlayer.getUniqueId(), bypassPlayer.getUniqueId());
	}
	
	public void removeBypass(Player nickedPlayer, Player... bypassPlayers) {
		if (nickedPlayer == null || !nickedPlayer.isOnline()) {
			return;
		}
		UUID[] uuidArr = new UUID[bypassPlayers.length];
		int count = 0;
		for (Player player : bypassPlayers) {
			uuidArr[count] = player.getUniqueId();
			++count;
		}
		this.removeBypass(nickedPlayer.getUniqueId(), uuidArr);
	}
	
	public void removeBypass(UUID nickedPlayerUUID, UUID bypassPlayerUUID) {
		this.removeBypass(nickedPlayerUUID, new UUID[]{bypassPlayerUUID});
	}
	
	public void removeBypass(UUID nickedPlayerUUID, UUID[] bypassPlayerUUIDs) {
		if (Bukkit.getPlayer((UUID) nickedPlayerUUID) == null || !Bukkit.getPlayer((UUID) nickedPlayerUUID).isOnline()) {
			return;
		}
		NickUser user = NickHandler.getUserByUUID(nickedPlayerUUID);
		for (UUID uuid : bypassPlayerUUIDs) {
			user.getBypassList().remove(uuid);
		}
	}
	
	public void clearBypass(Player nickedPlayer) {
		if (nickedPlayer == null || !nickedPlayer.isOnline()) {
			return;
		}
		NickHandler.getUser(nickedPlayer).getBypassList().clear();
	}
	
	public Player getPlayerOfOriginalName(String name) {
		for (NickUser user : NickHandler.getUsers()) {
			if (user == null || user.getOriginalName() == null || !user.getOriginalName().equalsIgnoreCase(name))
				continue;
			return user.getPlayer();
		}
		return null;
	}
	
	public Player getPlayerOfNickedName(String name) {
		for (NickUser user : NickHandler.getUsers()) {
			if (user == null || user.getNickProfile() == null || !user.getNickProfile().getName().equalsIgnoreCase(name))
				continue;
			return user.getPlayer();
		}
		return null;
	}
	
	public boolean nickExists(String name) {
		for (NickUser user : NickHandler.getUsers()) {
			if (user == null || user.getOriginalProfile() == null || user.getNickProfile() == null || !user.getOriginalProfile().getName().equalsIgnoreCase(name) && !user.getNickProfile().getName().equalsIgnoreCase(name) && !NickAPI.getOriginalName(user.getPlayer()).equalsIgnoreCase(name))
				continue;
			return true;
		}
		return false;
	}
	
	public boolean isNickedName(String name) {
		for (NickUser user : NickHandler.getUsers()) {
			if (user == null || user.getNickProfile() == null || user.getNickProfile().getName() == null || !user.getNickProfile().getName().equalsIgnoreCase(name) || NickAPI.getOriginalName(user.getPlayer()).equalsIgnoreCase(name))
				continue;
			return true;
		}
		return false;
	}
	
	public Map<UUID, String> getNickedPlayers() {
		HashMap<UUID, String> map = Maps.newHashMap();
		for (Player online : Bukkit.getOnlinePlayers()) {
			NickUser user = NickHandler.getUser(online);
			if (user == null || user.getOriginalName() == null || user.getNickProfile() == null || user.getOriginalName().equals(user.getNickProfile().getName()))
				continue;
			map.put(online.getUniqueId(), user.getNickProfile().getName());
		}
		return map;
	}
	
	public String getName(Player player) {
		if (player == null || !player.isOnline()) {
			return "null";
		}
		return NickHandler.getUser(player).getNickProfile().getName();
	}
	
	public void refreshPlayerSync(Player player, boolean async) {
		String key;
		NickUser user = NickHandler.getUser(player);
		if (user == null) {
			return;
		}
		Logger logger = Bukkit.getLogger();
		if (!player.isOnline()) {
			logger.log(Level.WARNING, "[NickAPI] Failed to nick player " + NickAPI.getOriginalName(player));
			logger.log(Level.WARNING, "[NickAPI] Player has not gone through PlayerJoinEvent");
			return;
		}
		if (user.getNickProfile() == null) {
			return;
		}
		if (!user.isNickAble()) {
			if (user.getNickAbleLoop() >= 5) {
				logger.log(Level.WARNING, "[NickAPI] Failed to nick player " + NickAPI.getOriginalName(player));
				logger.log(Level.WARNING, "[NickAPI] Tried several times to nick the player, but it failed because of the user data is not loaded correctly");
				logger.log(Level.WARNING, "[NickAPI] To prevent an infinite loop by calling delayed scheduler for an automatic fix, the nick process has been stopped now");
				logger.log(Level.WARNING, "[NickAPI] Please execute the method on PlayerJoinEvent at the earliest time");
				user.setNickAbleLoop(0);
				return;
			}
			Bukkit.getScheduler().runTaskLater((Plugin) NickPlugin.getPlugin(), () -> {
				if (player.isOnline()) {
					this.refreshPlayerSync(player, async);
					user.setNickAbleLoop(user.getNickAbleLoop() + 1);
				}
			}, 3L);
			return;
		}
		boolean respawnPacket = false;
		block22:
		for (Map.Entry<String, Object> entry : user.getQueueMap().entrySet()) {
			key = entry.getKey();
			Object value = entry.getValue();
			switch (key) {
				case "AddBypass": {
					UUID[] bypassPlayerArr;
					for (UUID uuid : bypassPlayerArr = (UUID[]) value) {
						Player target = Bukkit.getPlayer((UUID) uuid);
						if (target == null) continue;
						this.removeInfoPacket(player, target);
					}
					continue block22;
				}
				case "SetTag": {
					String tag = (String) value;
					Reflection.setField(user.getNickProfile(), "name", tag);
					respawnPacket = true;
					break;
				}
				case "SetUUID": {
					UUID uuid = UUID.randomUUID();
					if (value instanceof String) {
						String uuidName = (String) value;
						uuid = NickAPI.getUUIDFetcher().getUUIDByName(uuidName);
					}
					for (Player online : Bukkit.getOnlinePlayers()) {
						if (online == player) continue;
						this.removeInfoPacket(player, online);
					}
					if (value instanceof UUID) {
						uuid = (UUID) value;
					}
					Reflection.setField(user.getNickProfile(), "id", uuid);
					break;
				}
				case "SetSkin": {
					user.getNickProfile().getProperties().removeAll((Object) "textures");
					String[] data = null;
					if (value instanceof String[]) {
						data = (String[]) value;
					}
					if (value instanceof String) {
						UUID uuid;
						String name = (String) value;
						uuid = NickAPI.getUUIDFetcher().getUUIDByName(name);
						data = NickAPI.getSkinFetcher().getSkinDataByUUID(uuid);
						if (data[0].equals("NoValue") && data[1].equals("NoSignature")) {
							NickConfig config = NickAPI.getConfig();
							if (config.isSkinSettingsTexturesEnabled()) {
								data = new String[]{config.getSkinSettingsDefaultValue(), config.getSkinSettingsDefaultSignature()};
							} else {
								List<String> list = config.getSkinSettingsDefaultNames();
								String randomName = list.get(ThreadLocalRandom.current().nextInt(list.size()));
								uuid = NickAPI.getUUIDFetcher().getUUIDByName(randomName);
								data = NickAPI.getSkinFetcher().getSkinDataByUUID(uuid);
								if (data[0].equals("NoValue") && data[1].equals("NoSignature")) {
									data = new String[]{"ewogICJ0aW1lc3RhbXAiIDogMTYwMTU3NzgwNTM1MywKICAicHJvZmlsZUlkIiA6ICI4NTdmNGRkYjlkMTM0ZWZmYWE1Zjk0NWE0YWEyYWJmNiIsCiAgInByb2ZpbGVOYW1lIiA6ICIxMzM3RW1wdHkxMzM3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFhNGFmNzE4NDU1ZDRhYWI1MjhlN2E2MWY4NmZhMjVlNmEzNjlkMTc2OGRjYjEzZjdkZjMxOWE3MTNlYjgxMGIiCiAgICB9CiAgfQp9", "WFMBccFE/QDje43+G3cToZGasu6mYQW//QvN9J5u+wDPNq1S1DZylkyrLMywNuUV2xy7GXLJVYtY5MJteaWK2tkq43h3IheVVFGAyb6W9m56gOwKNItDU5IZs2JpZ/6W78HjhX3HOsPWYRXDW4GbRBpdKrpp+4FQfktPv1OabBEF0jrwMx2q7W1rw4s9BensyB1D3dJK0+mrnGFbJ4pZzn9WfjsNLBJrJZp5OCEwgLasK8s7Z4JoGETzErRdqcXVJZZe2Rt4GYWCzo+cSqYLeWrS7uuevVEoazpu/v8WlzcQ+TzGmRuZVYYcpZJHIgd+2qQPALCIEk8o0mKdWQ7QaBM/TMk1eiynaj2vofoVYuIbs3ye6WasgBmmjQbvISTwOFheecoJ9nAcRkdgfmj8zg2o8Sxju8Vb8nv9duTU7iJ3LrTQplgOZlJBz2fWGbwVZDvFkf2mVJPQASQgFl6nM6orBXgFkki3Xqq25kSfTJEj4ISO83yxq5ZSyUEuiB6ul/TrMBzDtBwcrZ8K3jHfHFCttZ6hffKKnOxF1iLWRDh0Gg9favqIdLYSQ9wEBEWG0lqBRNxOgl0itMx7Lr57f/1Mo6UpUscUSZUZt3zN+eF1NIwLy+C4NT9nQV93Hsqe/XPw9ZEuCaph6z5NRzILZBhPMi8Mn/n/mKYwS7l2Aq8="};
								}
							}
						}
					}
					if (data == null) break;
					user.setSkinData((String[]) data.clone());
					user.getNickProfile().getProperties().put((Object) "textures", (Object) new Property("textures", data[0], data[1]));
					respawnPacket = true;
				}
			}
		}
		for (Map.Entry<String, Object> entry : user.getQueueMap().entrySet()) {
			switch (key = entry.getKey()) {
				case "ResetUUID": {
					for (Player online : Bukkit.getOnlinePlayers()) {
						if (online == player) continue;
						this.removeInfoPacket(player, online);
					}
					Reflection.setField(user.getNickProfile(), "id", player.getUniqueId());
					break;
				}
				case "ResetSkin": {
					user.getNickProfile().getProperties().removeAll((Object) "textures");
					user.getNickProfile().getProperties().put((Object) "textures", (Object) new Property("textures", user.getOriginalSkinData()[0], user.getOriginalSkinData()[1]));
					user.setSkinData((String[]) user.getOriginalSkinData().clone());
					respawnPacket = true;
					break;
				}
				case "ResetTag": {
					Reflection.setField(user.getNickProfile(), "name", user.getOriginalName());
					respawnPacket = true;
				}
			}
		}
		user.getQueueMap().clear();
		if (async) {
			boolean finalRespawnPacket = respawnPacket;
			Bukkit.getScheduler().runTask((Plugin) NickPlugin.getPlugin(), () -> {
				if (!player.isOnline()) {
					return;
				}
				this.sendPackets(player, finalRespawnPacket, NickAPI.getConfig().isSkinChanging());
				ArrayList<Player> canSeeList = new ArrayList<Player>();
				for (Player online : Bukkit.getOnlinePlayers()) {
					if (!online.canSee(player)) continue;
					canSeeList.add(online);
					online.hidePlayer(player);
				}
				for (Player online : canSeeList) {
					online.showPlayer(player);
				}
				Bukkit.getPluginManager().callEvent((Event) new NickFinishEvent(player, user.getUuid(), user.getOriginalName(), user.getOriginalSkinData(), user.getNickProfile().getId(), user.getNickProfile().getName(), Reflection.getSkinData(user.getNickProfile()), user.getBypassList()));
			});
		} else {
			this.sendPackets(player, respawnPacket, NickAPI.getConfig().isSkinChanging());
			ArrayList<Player> canSeeList = new ArrayList<Player>();
			for (Player online : Bukkit.getOnlinePlayers()) {
				if (!online.canSee(player)) continue;
				canSeeList.add(online);
				online.hidePlayer(player);
			}
			for (Player online : canSeeList) {
				online.showPlayer(player);
			}
			Bukkit.getPluginManager().callEvent((Event) new NickFinishEvent(player, user.getUuid(), user.getOriginalName(), user.getOriginalSkinData(), user.getNickProfile().getId(), user.getNickProfile().getName(), Reflection.getSkinData(user.getNickProfile()), user.getBypassList()));
		}
	}
	
	public void refreshPlayer(Player player) {
		Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin) NickPlugin.getPlugin(), () -> this.refreshPlayerSync(player, true), 2L);
	}
	
	public void hidePlayer(Player player, Player toHide) {
		NickUser user = NickHandler.getUser(player);
		if (user == null) {
			return;
		}
		if (!player.isOnline() || !toHide.isOnline()) {
			return;
		}
		if (user.getFakeUUID() != null && user.getFakeUUID() != player.getUniqueId() && player != toHide) {
			this.removeInfoPacket(player, toHide);
		}
		if (player != toHide) {
			toHide.hidePlayer(player);
		}
		this.destroyPacket(player, toHide);
	}
	
	public void hidePlayerDelayed(Player player, Player toHide) {
		Bukkit.getScheduler().runTaskLater((Plugin) NickPlugin.getPlugin(), () -> this.hidePlayer(player, toHide), 1L);
	}
	
	public void hidePlayer(Player player, Collection<? extends Player> playersToHide) {
		for (Player player2 : playersToHide) {
			this.hidePlayerDelayed(player, player2);
		}
	}
	
	public void showPlayer(Player player, Player toShow) {
		toShow.showPlayer(player);
	}
	
	public void showPlayerDelayed(Player player, Player toShow) {
		Bukkit.getScheduler().runTaskLater((Plugin) NickPlugin.getPlugin(), () -> this.showPlayer(player, toShow), 1L);
	}
	
	public void showPlayer(Player player, Collection<? extends Player> playersToShow) {
		for (Player player2 : playersToShow) {
			this.showPlayer(player, player2);
		}
	}
	
	public abstract void removeInfoPacket(Player var1, Player var2);
	
	public abstract void destroyPacket(Player var1, Player var2);
	
	public abstract void sendPackets(Player var1, boolean var2, boolean var3);
}
