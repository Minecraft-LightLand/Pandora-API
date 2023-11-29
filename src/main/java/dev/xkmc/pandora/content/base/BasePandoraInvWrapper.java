package dev.xkmc.pandora.content.base;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.pandora.init.data.PandoraSlotGen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.UUID;

public class BasePandoraInvWrapper implements IItemHandlerModifiable {

	private final ItemStack stack;
	private final IPandoraHolder bag;
	private ListTag cachedTag;
	private List<ItemStack> itemStacksCache;

	public BasePandoraInvWrapper(ItemStack stack) {
		this.stack = stack;
		this.bag = (IPandoraHolder) stack.getItem();
	}

	@Override
	public int getSlots() {
		return bag.getSlots(stack);
	}

	@Override
	@NotNull
	public ItemStack getStackInSlot(int slot) {
		validateSlotIndex(slot);
		return getItemList().get(slot);
	}

	@Override
	@NotNull
	public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
			return ItemStack.EMPTY;

		if (!isItemValid(slot, stack))
			return stack;

		validateSlotIndex(slot);

		List<ItemStack> itemStacks = getItemList();

		ItemStack existing = itemStacks.get(slot);

		int limit = Math.min(getSlotLimit(slot), stack.getMaxStackSize());

		if (!existing.isEmpty()) {
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
				return stack;

			limit -= existing.getCount();
		}

		if (limit <= 0)
			return stack;

		boolean reachedLimit = stack.getCount() > limit;

		if (!simulate) {
			if (existing.isEmpty()) {
				itemStacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
			} else {
				existing.grow(reachedLimit ? limit : stack.getCount());
			}
			setItemList(itemStacks);
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Override
	@NotNull
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		List<ItemStack> itemStacks = getItemList();
		if (amount == 0)
			return ItemStack.EMPTY;

		validateSlotIndex(slot);

		ItemStack existing = itemStacks.get(slot);

		if (existing.isEmpty())
			return ItemStack.EMPTY;

		int toExtract = Math.min(amount, existing.getMaxStackSize());

		if (existing.getCount() <= toExtract) {
			if (!simulate) {
				itemStacks.set(slot, ItemStack.EMPTY);
				setItemList(itemStacks);
				return existing;
			} else {
				return existing.copy();
			}
		} else {
			if (!simulate) {
				itemStacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
				setItemList(itemStacks);
			}

			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}

	private void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= getSlots())
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return stack.isEmpty() || bag.isItemValid(slot, stack);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		validateSlotIndex(slot);
		if (!isItemValid(slot, stack)) throw new RuntimeException("Invalid stack " + stack + " for slot " + slot + ")");
		List<ItemStack> itemStacks = getItemList();
		itemStacks.set(slot, stack);
		setItemList(itemStacks);
	}

	private List<ItemStack> getItemList() {
		ListTag rootTag = IPandoraHolder.getListTag(stack);
		if (itemStacksCache == null || cachedTag == null || !cachedTag.equals(rootTag))
			itemStacksCache = refreshItemList(rootTag);
		return itemStacksCache;
	}

	private List<ItemStack> refreshItemList(ListTag rootTag) {
		List<ItemStack> list = IPandoraHolder.getItems(stack);
		int size = getSlots();
		while (list.size() < size) {
			list.add(ItemStack.EMPTY);
		}
		cachedTag = rootTag;
		return list;
	}

	private void setItemList(List<ItemStack> itemStacks) {
		IPandoraHolder.setItems(stack, itemStacks);
		cachedTag = IPandoraHolder.getListTag(stack);
		itemStacksCache = null;
	}

}