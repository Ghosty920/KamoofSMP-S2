package me.ghosty.kamoof.features.autoupdate;

import com.google.gson.*;
import me.ghosty.kamoof.KamoofSMP;
import me.ghosty.kamoof.utils.*;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UpdateChecker implements Listener {
	
	private boolean hasUpdate = false;
	private String currentVersion, newVersion, changelog, downloads, updateLink;
	
	private BaseComponent[] message;
	
	public UpdateChecker() {
		currentVersion = KamoofSMP.getInstance().getDescription().getVersion().trim().toLowerCase();
		boolean download = KamoofSMP.config().getBoolean("autoupdate.download");
		Bukkit.getScheduler().runTaskAsynchronously(KamoofSMP.getInstance(), () -> {
			if(checkForUpdate() && (!download || !downloadUpdate(updateLink))) {
				hasUpdate = true;
				Bukkit.getConsoleSender().spigot().sendMessage(this.message);
			}
		});
	}
	
	public boolean checkForUpdate() {
		try {
			String data = HTTPUtils.get("https://api.modrinth.com/v2/project/kamoofsmp2/version",
				new HashMap<>() {{
					put("User-Agent", "github: @Ghosty920/KamoofSMP-S2/v" + currentVersion);
				}}).response();
			JsonObject object = JsonParser.parseString(data).getAsJsonArray().get(0).getAsJsonObject();
			newVersion = object.get("version_number").getAsString().trim().toLowerCase();
			if (currentVersion != newVersion) {
				
				int downloadsCount = object.get("downloads").getAsInt();
				if (downloadsCount < 1_000)
					downloads = String.valueOf(downloadsCount);
				else if (downloadsCount < 1_000_000)
					downloads = String.format("%.2fk", (float) (downloadsCount / 1_000));
				else
					downloads = String.format("%.2fM", (float) (downloadsCount / 1_000_000)); // why not
				
				// idc of what u could say, as long as it works!!
				changelog = object.get("changelog").getAsString().replace("\n", "\\n");
				Pattern pattern = Pattern.compile(Lang.VERSION_CHANGELOG_REGEX.get(), Pattern.CANON_EQ);
				Matcher matcher = pattern.matcher(changelog);
				matcher.find();
				changelog = String.join("<br>", matcher.group().split("\\\\n"));
				changelog = changelog.replace("```", "");
				
				String hover = String.format(Lang.NEW_VERSION_HOVER.get(), newVersion, downloads, changelog);
				String url = "https://modrinth.com/plugin/kamoofsmp2/version/" + newVersion;
				String message = String.format(Lang.NEW_VERSION.get(), hover, url, currentVersion, newVersion);
				this.message = Message.toBaseComponent(message);
				
				updateLink = getFileURL(object);
				return true;
			}
		} catch (Throwable exc) {
			exc.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(Lang.UPDATE_CHECKER_FAIL.get());
		}
		return false;
	}
	
	public boolean downloadUpdate(String url) {
		if(url == null)
			return false;
		try {
			HTTPUtils.RawResponse response = HTTPUtils.getRaw(url,
				new HashMap<>() {{
					put("User-Agent", "github: @Ghosty920/KamoofSMP-S2/v" + currentVersion);
				}});
			URL location = KamoofSMP.class.getProtectionDomain().getCodeSource().getLocation();
			Files.write(Path.of(location.toURI()), response.response());
			
			String hover = String.format(Lang.NEW_VERSION_HOVER.get(), newVersion, downloads, changelog);
			String uUrl = "https://modrinth.com/plugin/kamoofsmp2/version/" + newVersion;
			String message = String.format(Lang.NEW_VERSION_DOWNLOADED.get(), hover, uUrl, currentVersion, newVersion);
			this.message = Message.toBaseComponent(message);
			
			hasUpdate = true;
			Bukkit.getConsoleSender().spigot().sendMessage(this.message);
			
			return true;
		} catch(Throwable exc) {
			exc.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(Lang.UPDATE_DOWNLOAD_FAIL.get());
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
	
}
