package dev.xkmc.pandora.content.core;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class InheritedItemStackHandler implements IItemHandler, IItemHandlerModifiable {

	private final CombinedStackData data;
	protected int actualSize;

	public InheritedItemStackHandler(ICuriosItemHandler parent, int size, boolean isCosmetic) {
		this.actualSize = size;
		data = new CombinedStackData(parent, isCosmetic);
		data.buildStackData(size);
	}

	protected void resizedList(int i) {
		actualSize += i;
		data.buildStackData(actualSize);
	}

	protected void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= actualSize)
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + actualSize + ")");
	}

	protected void onContentsChanged(int slot) {

	}


	@Override
	public void setStackInSlot(int slot, @NotNull ItemStack stack) {
		int index = data.getIndexForSlot(slot);
		IItemHandlerModifiable handler = data.getHandlerFromIndex(index);
		slot = data.getSlotFromIndex(slot, index);
		handler.setStackInSlot(slot, stack);
	}

	@Override
	public int getSlots() {
		return actualSize;
	}

	@Override
	@NotNull
	public ItemStack getStackInSlot(int slot) {
		int index = data.getIndexForSlot(slot);
		IItemHandlerModifiable handler = data.getHandlerFromIndex(index);
		slot = data.getSlotFromIndex(slot, index);
		return handler.getStackInSlot(slot);
	}

	@Override
	@NotNull
	public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		int index = data.getIndexForSlot(slot);
		IItemHandlerModifiable handler = data.getHandlerFromIndex(index);
		slot = data.getSlotFromIndex(slot, index);
		return handler.insertItem(slot, stack, simulate);
	}

	@Override
	@NotNull
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		int index = data.getIndexForSlot(slot);
		IItemHandlerModifiable handler = data.getHandlerFromIndex(index);
		slot = data.getSlotFromIndex(slot, index);
		return handler.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		int index = data.getIndexForSlot(slot);
		IItemHandlerModifiable handler = data.getHandlerFromIndex(index);
		int localSlot = data.getSlotFromIndex(slot, index);
		return handler.getSlotLimit(localSlot);
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		int index = data.getIndexForSlot(slot);
		IItemHandlerModifiable handler = data.getHandlerFromIndex(index);
		int localSlot = data.getSlotFromIndex(slot, index);
		return handler.isItemValid(localSlot, stack);
	}

}
