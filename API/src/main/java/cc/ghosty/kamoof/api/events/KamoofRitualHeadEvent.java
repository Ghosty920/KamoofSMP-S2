package cc.ghosty.kamoof.api.events;

import lombok.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
@Setter
public final class KamoofRitualHeadEvent extends Event implements Cancellable {
	
	@Getter
	private static final HandlerList handlerList = new HandlerList();
	
	private final ItemStack head;
	private final ArmorStand stand;
	
	private boolean cancelled;
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
}
