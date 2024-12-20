package im.ghosty.kamoof.api.events;

import lombok.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Event} {@link Cancellable} appellé quand un joueur place une tête sur un socle du rituel. (voir RitualListener#onPlaceHead)
 * @since 1.4
 */
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public final class KamoofRitualHeadEvent extends Event implements Cancellable {
	
	@Getter
	private static final HandlerList handlerList = new HandlerList();
	
	/**
	 * Le {@link Player} qui place la tête.
	 */
	private final Player player;
	
	/**
	 * L'{@link ItemStack} qui représente la tête.
	 */
	private final ItemStack head;
	
	/**
	 * L'{@link ArmorStand} sur lequel est placé la tête.
	 */
	private final ArmorStand stand;
	
	/**
	 * Si le rituel sera lancé après que la tête soit placée.
	 */
	private final boolean canRunRitual;
	
	/**
	 * Si l'événement est annulé.
	 */
	private boolean cancelled;
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
}
