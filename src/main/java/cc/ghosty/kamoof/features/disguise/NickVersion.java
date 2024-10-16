package cc.ghosty.kamoof.features.disguise;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.function.Supplier;

@AllArgsConstructor
public enum NickVersion {
	
	v6(() -> {
		try {
			NickAPI.class.getMethod("nick", Player.class, String.class);
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}),
	v7(() -> {
		try {
			NickAPI.class.getMethod("setNick", Player.class, String.class);
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	});
	
	public final Supplier<Boolean> check;
	
	private static NickVersion version;
	
	public static NickVersion get() {
		if (version == null) {
			for (NickVersion ver : values()) {
				if (!ver.check.get())
					continue;
				version = ver;
				break;
			}
			if (version == null)
				throw new RuntimeException("Cannot find NickAPI's version.");
		}
		return version;
	}
	
}
