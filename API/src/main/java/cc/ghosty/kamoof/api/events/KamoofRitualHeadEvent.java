package cc.ghosty.kamoof.api.events;

import lombok.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Event} {@link Cancellable} appellé quand un joueur place une tête sur un socle du rituel. (voir RitualListener#onPlaceHead)
 */
@RequiredArgsConstructor
@Getter
@Setter
public final class KamoofRitualHeadEvent extends Event implements Cancellable {
	
	@Getter
	private static final HandlerList handlerList = new HandlerList();
	
	private final Player player;
	private final ItemStack head;
	private final ArmorStand stand;
	private final boolean canRunRitual;
	
	private boolean cancelled;
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
}
