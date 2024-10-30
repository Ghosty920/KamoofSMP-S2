package cc.ghosty.kamoof.gui;

import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Getter
public final class StringGUI implements InventoryHolder {
	
	private final String title, defaultName;
	private final Predicate<String> predicate;
	
	public Consumer<InventoryCloseEvent> onClose;
	public Consumer<InventoryClickEvent> onAccept;
	
	public boolean accepted = false;
	private boolean alreadyClosed = false;
	
	public StringGUI setOnClose(Consumer<InventoryCloseEvent> onClose) {
		this.onClose = onClose;
		return this;
	}
	
	public StringGUI setOnAccept(Consumer<InventoryClickEvent> onAccept) {
		this.onAccept = onAccept;
		return this;
	}
	
	@Override
	public Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(this, InventoryType.ANVIL, title);
		
		ItemStack item = new ItemStack(Material.NAME_TAG);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		meta.setDisplayName(defaultName);
		item.setItemMeta(meta);
		inv.setItem(0, item);
		
		return inv;
	}
	
	public static final class EventListener implements Listener {
		
		@EventHandler
		public void onResult(PrepareInventoryResultEvent event) {
			System.out.println("3" + event.getInventory().getHolder());
			if (event.getInventory().getHolder() == null || !(event.getInventory().getHolder() instanceof StringGUI gui))
				return;
			System.out.println("3");
			event.getView().getPlayer().sendMessage(Joiner.on(",").withKeyValueSeparator("=").join(event.getResult().serialize()));
			if (gui.predicate.test(event.getResult().getItemMeta().getDisplayName()))
				event.getResult().setAmount(0);
			else
				event.getView().setRepairCost(0);
		}
		
		@EventHandler
		public void onClick(InventoryClickEvent event) {
			if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.SHIFT_LEFT)
				return;
			if (event.getClickedInventory().getHolder() == null || !(event.getClickedInventory().getHolder() instanceof StringGUI gui))
				return;
			if (event.getSlot() == 2 || event.getCursor() != null) {
//				event.getView().getPlayer().sendMessage("1-"+Joiner.on(",").withKeyValueSeparator("=").join(event.getCursor().serialize()));
//				event.getView().getPlayer().sendMessage("2-"+Joiner.on(",").withKeyValueSeparator("=").join(event.getCurrentItem().serialize()));
//				event.getView().getPlayer().sendMessage("3-"+Joiner.on(",").withKeyValueSeparator("=").join(event.getClickedInventory().getItem(2).serialize()));
				gui.accepted = true;
				if (gui.onAccept != null)
					gui.onAccept.accept(event);
				event.getWhoClicked().closeInventory();
			}
			event.setResult(Event.Result.DENY);
		}
		
		@EventHandler
		public void onClose(InventoryCloseEvent event) {
			System.out.println("1" + event.getInventory().getHolder());
			if (event.getInventory().getHolder() == null || !(event.getInventory().getHolder() instanceof StringGUI gui))
				return;
			System.out.println("1");
			if (gui.alreadyClosed)
				return;
			gui.alreadyClosed = true;
			if (gui.onClose != null)
				gui.onClose.accept(event);
		}
		
	}
	
}
