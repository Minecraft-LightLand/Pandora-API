package dev.xkmc.pandora.content.base;

import dev.xkmc.pandora.init.data.PandoraTagGen;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.items.ComponentItemHandler;

public class PandoraItemHandler extends ComponentItemHandler {
	private final IPandoraHolder bag;
	private final ItemStack stack;

	public PandoraItemHandler(ItemStack parent, DataComponentType<ItemContainerContents> type) {
		super(parent, type, 0);
		bag = (IPandoraHolder) parent.getItem();
		stack = parent;
	}

	public int getSlots() {
		return bag.getSlots(stack);
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	public boolean isItemValid(int slot, ItemStack stack) {
		if (stack.isEmpty()) return true;
		if (!bag.isItemValid(slot, stack)) return false;
		if (stack.is(PandoraTagGen.ALLOW_DUPLICATE)) return true;
		var content = getContents();
		int n = getSlots();
		for (int i = 0; i < n; i++) {
			if (i == slot) continue;
			var item = getStackFromContents(content, i);
			if (item.isEmpty()) continue;
			if (item.getItem() == stack.getItem())
				return false;
		}
		return true;
	}

}
