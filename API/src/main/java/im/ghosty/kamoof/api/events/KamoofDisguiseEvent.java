package im.ghosty.kamoof.api.events;

import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Event} {@link Cancellable} appelé quand un joueur se déguise en un autre.
 *
 * @since 1.4.2
 */
@RequiredArgsConstructor
@Getter
@ToString
public final class KamoofDisguiseEvent extends Event implements Cancellable {
	
	@Getter
	private static final HandlerList handlerList = new HandlerList();
	
	/**
	 * Le {@link Player} qui souhaîte se déguiser.
	 */
	private final Player player;
	
	/**
	 * Le nom en lequel le joueur veut se déguiser.
	 */
	private final String disguise;
	
	/**
	 * Si l'événement est annulé.
	 */
	private boolean cancelled;
	
	/**
	 * @param cancelled Si l'événement est annulé.
	 */
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
}
