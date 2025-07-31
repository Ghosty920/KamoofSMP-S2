package im.ghosty.kamoof.api.events;

import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Event} appelé quand un joueur retire son déguisement.
 *
 * @since 1.5
 */
@RequiredArgsConstructor
@Getter
@ToString
public final class KamoofUndisguiseEvent extends Event {
	
	@Getter
	private static final HandlerList handlerList = new HandlerList();
	
	/**
	 * Le {@link Player} qui souhaîte retirer son déguisement.
	 */
	private final Player player;
	
	/**
	 * Le nom en lequel le joueur veut se déguiser.
	 */
	private final String disguise;
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
}
