package dev.xkmc.pandora.content.menu.edit;

import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.util.annotation.ServerOnly;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import dev.xkmc.l2tabs.compat.BaseCuriosListMenu;
import dev.xkmc.pandora.content.base.IPandoraHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class PandoraEditMenu extends BaseContainerMenu<PandoraEditMenu> {

	private static SpriteManager getManager(int size) {
		int row = (size + 8) / 9;
		return BaseCuriosListMenu.MANAGER[Math.min(Math.max(row - 3, 0), 3)];
	}

	public static PandoraEditMenu fromNetwork(MenuType<PandoraEditMenu> type, int windowId, Inventory inv, FriendlyByteBuf buf) {
		PlayerSlot<?> slot = PlayerSlot.read(buf);
		int row = buf.readInt();
		return new PandoraEditMenu(type, windowId, inv, slot, row);
	}

	protected final Player player;
	public final PlayerSlot<?> item_slot;
	protected final IItemHandlerModifiable handler;

	public PandoraEditMenu(MenuType<PandoraEditMenu> type, int windowId, Inventory inventory,
						   PlayerSlot<?> hand, int size) {
		super(type, windowId, inventory, getManager(size), menu -> new SimpleContainer(0), false);
		this.item_slot = hand;
		this.player = inventory.player;
		ItemStack stack = getStack();
		this.handler = ((IPandoraHolder) stack.getItem()).getCap(stack).get();
		this.addSlot("grid", handler.getSlots());
	}

	protected void addSlot(String name, int size) {
		int current = added;
		this.sprite.get().getSlot(name, (x, y) -> added - current < size ? new PandoraSlot(handler, this.added++, x, y) : null, this::addSlot);
	}

	private ItemStack stack_cache = ItemStack.EMPTY;

	@ServerOnly
	@Override
	public boolean stillValid(Player player) {
		ItemStack oldStack = stack_cache;
		ItemStack newStack = getStackRaw();
		if (getStackRaw().isEmpty() || oldStack != newStack) {
			return false;
		}
		ItemStack stack = getStack();
		if (stack.getItem() instanceof IPandoraHolder holder) {
			var opt = holder.getCap(stack);
			return opt.isPresent() && opt.get() == handler;
		}
		return false;
	}

	public ItemStack getStack() {
		ItemStack stack = getStackRaw();
		if (stack.isEmpty()) return stack_cache;
		return stack;
	}

	private ItemStack getStackRaw() {
		ItemStack stack = item_slot.getItem(player);
		if (player.level().isClientSide()) return stack;
		stack_cache = stack;
		return stack;
	}

	public ItemStack quickMoveStack(Player pl, int id) {
		ItemStack stack = this.slots.get(id).getItem();
		int n = this.handler.getSlots();
		boolean moved;
		if (id >= 36) {
			moved = this.moveItemStackTo(stack, 0, 36, true);
		} else {
			moved = this.moveItemStackTo(stack, 36, 36 + n, false);
		}
		if (moved) {
			this.slots.get(id).setChanged();
		}
		return ItemStack.EMPTY;
	}

}
