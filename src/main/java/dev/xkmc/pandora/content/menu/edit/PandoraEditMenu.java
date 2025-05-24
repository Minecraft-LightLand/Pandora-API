package dev.xkmc.pandora.content.menu.edit;

import dev.xkmc.l2core.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import dev.xkmc.l2menustacker.click.writable.ContainerCallback;
import dev.xkmc.l2menustacker.screen.source.PlayerSlot;
import dev.xkmc.l2tabs.compat.common.BaseCuriosListMenu;
import dev.xkmc.pandora.content.base.IPandoraHolder;
import dev.xkmc.pandora.init.Pandora;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class PandoraEditMenu extends BaseContainerMenu<PandoraEditMenu> {

	private static SpriteManager getManager(int size) {
		int row = (size + 8) / 9;
		return BaseCuriosListMenu.MANAGER[Math.min(Math.max(row - 3, 0), 3)];
	}

	public static PandoraEditMenu fromNetwork(MenuType<PandoraEditMenu> type, int windowId, Inventory inv, RegistryFriendlyByteBuf buf) {
		PlayerSlot<?> slot = PlayerSlot.read(buf);
		int row = buf.readInt();
		return new PandoraEditMenu(type, windowId, inv, slot, row, null);
	}

	protected final Player player;
	public final PlayerSlot<?> item_slot;
	protected final IItemHandlerModifiable handler;
	private final @Nullable ContainerCallback callback;

	public PandoraEditMenu(MenuType<PandoraEditMenu> type, int windowId, Inventory inventory,
						   PlayerSlot<?> hand, int size, @Nullable ContainerCallback callback) {
		super(type, windowId, inventory, getManager(size), menu -> new SimpleContainer(0), false);
		this.item_slot = hand;
		this.player = inventory.player;
		this.callback = callback;
		ItemStack stack = getStack();
		IItemHandlerModifiable candidate = null;
		if (stack.getItem() instanceof IPandoraHolder holder) {
			var opt = holder.getCap(stack);
			if (opt.isPresent()) {
				candidate = opt.get();
			}
		}
		this.handler = candidate != null ? candidate : new PandoraInvWrapper(new SimpleContainer(size));
		this.addSlot("grid", handler.getSlots());

	}

	protected void addSlot(String name, int size) {
		int current = added;
		getLayout().getSlot(name, (x, y) -> added - current < size ? new SlotItemHandler(handler, this.added++, x, y) : null, this::addSlot);
	}

	private ItemStack stack_cache = ItemStack.EMPTY;

	@Override
	public boolean stillValid(Player player) {
		ItemStack oldStack = this.stack_cache;
		ItemStack newStack = this.getStackRaw();
		return !this.getStackRaw().isEmpty() && oldStack == newStack;
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
			this.slots.get(id).set(stack);
		}
		return ItemStack.EMPTY;
	}


	protected boolean moveItemStackTo(ItemStack stack, int start, int end, boolean reverse) {
		if (stack.isEmpty()) return false;
		boolean flag = false;
		int i;
		if (reverse) {
			i = end - 1;
		} else {
			i = start;
		}
		while (true) {
			if (reverse) {
				if (i < start) break;
			} else if (i >= end) break;
			Slot slot = this.slots.get(i);
			ItemStack inSlot = slot.getItem();
			if (inSlot.isEmpty() && slot.mayPlace(stack)) {
				int l = slot.getMaxStackSize(stack);
				slot.setByPlayer(stack.split(Math.min(stack.getCount(), l)));
				slot.setChanged();
				flag = true;
				if (stack.isEmpty())
					break;
			}
			if (reverse) --i;
			else ++i;
		}
		return flag;
	}

}
