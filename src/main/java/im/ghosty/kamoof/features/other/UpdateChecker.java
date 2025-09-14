package im.ghosty.kamoof.features.other;

import im.ghosty.kamoof.KamoofPlugin;
import im.ghosty.kamoof.features.Feature;
import com.google.gson.*;
import im.ghosty.kamoof.utils.*;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static im.ghosty.kamoof.KamoofPlugin.config;

public final class UpdateChecker extends Feature {
	
	private final String currentVersion;
	private boolean hasUpdate = false;
	private String newVersion;
	private String changelog;
	private String downloads;
	private String updateLink;
	
	private BaseComponent[] message;
	private BukkitTask task;
	
	@Override
	public boolean isEnabled() {
		return config().getBoolean("autoupdate.fetch");
	}
	
	@Override
	public void onEnable() {
		super.onEnable();

		final boolean[] shouldRestart = {true};
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(KamoofPlugin.getInstance(), () -> {
			boolean download = config().getBoolean("autoupdate.download");
			if (checkForUpdate() && (!download || !downloadUpdate(updateLink, shouldRestart[0]))) {
				hasUpdate = true;
				Bukkit.getConsoleSender().spigot().sendMessage(this.message);
			}
			shouldRestart[0] = false;
		}, 1L, 24 * 60 * 60 * 20L);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		task.cancel();
		task = null;
	}
	
	public UpdateChecker() {
		currentVersion = KamoofPlugin.getInstance().getDescription().getVersion().trim().toLowerCase();
	}
	
	public boolean checkForUpdate() {
		try {
			sendMessage("UPDATE_CHECKING");
			String data = HTTPUtils.get("https://api.modrinth.com/v2/project/camouf2/version",
				new HashMap<>() {{
					put("User-Agent", "github: @Ghosty920/KamoofSMP-S2/v" + currentVersion);
				}}).response();
			JsonObject object = JsonParser.parseString(data).getAsJsonArray().get(0).getAsJsonObject();
			newVersion = object.get("version_number").getAsString().trim().toLowerCase();
			
			if (Arrays.compare(currentVersion.getBytes(), newVersion.getBytes()) != 0) {
				
				int downloadsCount = object.get("downloads").getAsInt();
				if (downloadsCount < 1_000)
					downloads = String.valueOf(downloadsCount);
				else if (downloadsCount < 1_000_000)
					downloads = String.format("%.2fk", (float) (downloadsCount / 1_000));
				else
					downloads = String.format("%.2fM", (float) (downloadsCount / 1_000_000)); // why not
				
				// idc of what u could say, as long as it works!!
				changelog = object.get("changelog").getAsString().replace("\n", "\\n");
				Pattern pattern = Pattern.compile(Lang.get("VERSION_CHANGELOG_REGEX"), Pattern.CANON_EQ);
				Matcher matcher = pattern.matcher(changelog);
				matcher.find();
				changelog = matcher.group().replace("\\n", "<br>").replace("\"", "\\\"");
				
				String hover = String.format(Lang.get("NEW_VERSION_HOVER"), newVersion, downloads, changelog);
				String url = "https://modrinth.com/plugin/camouf2/version/" + newVersion;
				String message = String.format(Lang.get("NEW_VERSION"), hover, url, currentVersion, newVersion);
				this.message = Message.toBaseComponent(message);
				
				updateLink = getFileURL(object);
				return true;
			}
			
			sendMessage("UPDATE_ALREADY_LAST");
		} catch (Throwable exc) {
			exc.printStackTrace();
			sendMessage("UPDATE_CHECKER_FAIL");
		}
		return false;
	}
	
	public boolean downloadUpdate(String url, boolean restart) {
		if (url == null)
			return false;
		try {
			sendMessage("UPDATE_DOWNLOADING");
			HTTPUtils.RawResponse response = HTTPUtils.getRaw(url,
				new HashMap<>() {{
					put("User-Agent", "github: @Ghosty920/KamoofSMP-S2/v" + currentVersion);
				}});
			
			URL location = KamoofPlugin.class.getProtectionDomain().getCodeSource().getLocation();
			File pluginFile = Utils.getParentPluginFile(location);
			Files.write(Path.of(pluginFile.toURI()), response.response());
			
			sendMessage("UPDATE_DOWNLOADED");
			
			if (!restart) {
				
				String hover = String.format(Lang.get("NEW_VERSION_HOVER"), newVersion, downloads, changelog);
				String uUrl = "https://modrinth.com/plugin/camouf2/version/" + newVersion;
				String message = String.format(Lang.get("NEW_VERSION_DOWNLOADED"), hover, uUrl, currentVersion, newVersion);
				this.message = Message.toBaseComponent(message);
				
				hasUpdate = true;
				Bukkit.getConsoleSender().spigot().sendMessage(this.message);
				
			} else {
				Bukkit.getScheduler().runTask(KamoofPlugin.getInstance(),
					() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart"));
			}
			
			
			return true;
		} catch (Throwable exc) {
			exc.printStackTrace();
			sendMessage("UPDATE_DOWNLOAD_FAIL");
		}
		return false;
	}
	
	public String getFileURL(JsonObject obj) {
		JsonArray files = obj.get("files").getAsJsonArray();
		for (int i = 0; i < files.size(); i++) {
			JsonObject file = files.get(i).getAsJsonObject();
			if (!file.get("primary").getAsBoolean())
				continue;
			return file.get("url").getAsString();
		}
		return null;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (!hasUpdate || message == null)
			return;
		Player player = event.getPlayer();
		if (!player.hasPermission("kamoofsmp.admin"))
			return;
		
		player.spigot().sendMessage(message);
	}
	
	private void sendMessage(String key, Object... args) {
		Lang.send(Bukkit.getConsoleSender(), key, args);
	}
	
}
