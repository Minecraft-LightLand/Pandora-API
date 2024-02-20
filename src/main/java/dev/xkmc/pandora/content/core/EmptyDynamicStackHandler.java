package dev.xkmc.pandora.content.core;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public record EmptyDynamicStackHandler(int size) implements IDynamicStackHandler {

	@Override
	public void setStackInSlot(int slot, @NotNull ItemStack stack) {

	}

	@NotNull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		return stack;
	}

	@Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 0;
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		return false;
	}

	@Override
	public void setPreviousStackInSlot(int slot, @NotNull ItemStack stack) {

	}

	@Override
	public ItemStack getPreviousStackInSlot(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlots() {
		return size;
	}

	@Override
	public void grow(int amount) {

	}

	@Override
	public void shrink(int amount) {

	}

	@Override
	public CompoundTag serializeNBT() {
		return new CompoundTag();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {

	}
}
