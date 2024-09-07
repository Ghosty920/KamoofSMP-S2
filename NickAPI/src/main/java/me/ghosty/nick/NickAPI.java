package me.ghosty.nick;

import java.util.Map;
import java.util.UUID;

public class NickAPI {
	
	private static final AImplement implementation = NickPlugin.getPlugin().getHandler().getAImplement();
	
	private static final UUIDFetcher uuidFetcher = new UUIDFetcher();
	private static final SkinFetcher skinFetcher = new SkinFetcher();
	
	public static void nick(Player player, String toNick) {
		implementation.nick(player, toNick);
	}
	
	public static void resetNick(Player player) {
		implementation.resetNick(player);
	}
	
	public static boolean isNicked(Player player) {
		return implementation.isNicked(player);
	}
	
	public static boolean isSkinChanged(Player player) {
		return implementation.isSkinChanged(player);
	}
	
	public static void setSkin(Player player, String toSkin) {
		implementation.setSkin(player, toSkin);
	}
	
	public static void setSkin(Player player, String value, String signature) {
		implementation.setSkin(player, value, signature);
	}
	
	public static void resetSkin(Player player) {
		implementation.resetSkin(player);
	}
	
	public static String[] getSkinData(Player player) {
		return implementation.getSkinData(player);
	}
	
	public static String getOriginalGameProfileName(Player player) {
		return implementation.getOriginalGameProfileName(player);
	}
	
	public static String getOriginalName(Player player) {
		return implementation.getOriginalGameProfileName(player);
	}
	
	public static String getGameProfileName(Player player) {
		return implementation.getGameProfileName(player);
	}
	
	public static void setGameProfileName(Player player, String name) {
		implementation.setGameProfileName(player, name);
	}
	
	public static void resetGameProfileName(Player player) {
		implementation.resetGameProfileName(player);
	}
	
	public static UUID getUniqueId(Player player) {
		return implementation.getUniqueId(player);
	}
	
	public static void setUniqueId(Player player, UUID uuid) {
		implementation.setUniqueId(player, uuid);
	}
	
	public static void setUniqueId(Player player, String name) {
		implementation.setUniqueId(player, name);
	}
	
	public static void resetUniqueId(Player player) {
		implementation.resetUniqueId(player);
	}
	
	public static void refreshPlayer(Player player) {
		implementation.refreshPlayer(player);
	}
	
	public static void refreshPlayerSync(Player player) {
		implementation.refreshPlayerSync(player, false);
	}
	
	public static Player getPlayerOfOriginalName(String name) {
		return implementation.getPlayerOfOriginalName(name);
	}
	
	public static Player getPlayerOfNickedName(String name) {
		return implementation.getPlayerOfNickedName(name);
	}
	
	public static boolean nickExists(String name) {
		return implementation.nickExists(name);
	}
	
	public static boolean isNickedName(String name) {
		return implementation.isNickedName(name);
	}
	
	public static Map<UUID, String> getNickedPlayers() {
		return implementation.getNickedPlayers();
	}
	
	public static String getName(Player player) {
		return implementation.getName(player);
	}
	
}
