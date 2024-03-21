package dev.xkmc.pandora.content.core;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class PandoraDynamicStackHandler extends InheritedItemStackHandler implements IDynamicStackHandler {

	protected NonNullList<ItemStack> previousStacks;
	protected Function<Integer, SlotContext> ctxBuilder;

	public PandoraDynamicStackHandler(ICuriosItemHandler parent, int size, boolean isCosmetic, Function<Integer, SlotContext> ctxBuilder) {
		super(parent, size, isCosmetic);
		this.previousStacks = NonNullList.withSize(size, ItemStack.EMPTY);
		this.ctxBuilder = ctxBuilder;
	}

	@Override
	public void setPreviousStackInSlot(int slot, @Nonnull ItemStack stack) {
		if (!validateSlotIndex(slot)) return;
		if (slot < 0 || slot >= previousStacks.size()) return;
		this.previousStacks.set(slot, stack);
		this.onContentsChanged(slot);
	}

	@Nonnull
	@Override
	public ItemStack getPreviousStackInSlot(int slot) {
		if (!validateSlotIndex(slot)) return ItemStack.EMPTY;
		if (slot < 0 || slot >= previousStacks.size()) return ItemStack.EMPTY;
		return this.previousStacks.get(slot);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		if (slot < 0 || slot >= getSlots()) return false;
		SlotContext ctx = ctxBuilder.apply(slot);
		CurioEquipEvent equipEvent = new CurioEquipEvent(stack, ctx);
		MinecraftForge.EVENT_BUS.post(equipEvent);
		Event.Result result = equipEvent.getResult();
		if (result == Event.Result.DENY) {
			return false;
		}
		return result == Event.Result.ALLOW || (CuriosApi.isStackValid(ctx, stack) &&
				CuriosApi.getCurio(stack).map(curio -> curio.canEquip(ctx)).orElse(true) &&
				super.isItemValid(slot, stack));
	}

	@Override
	public @Nonnull ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack existing = this.getStackInSlot(slot);
		SlotContext ctx = ctxBuilder.apply(slot);
		CurioUnequipEvent unequipEvent = new CurioUnequipEvent(existing, ctx);
		MinecraftForge.EVENT_BUS.post(unequipEvent);
		Event.Result result = unequipEvent.getResult();

		if (result == Event.Result.DENY) {
			return ItemStack.EMPTY;
		}
		boolean isCreative = ctx.entity() instanceof Player player && player.isCreative();

		if (result == Event.Result.ALLOW ||
				((existing.isEmpty() || isCreative || !EnchantmentHelper.hasBindingCurse(existing)) &&
						CuriosApi.getCurio(existing).map(curio -> curio.canUnequip(ctx)).orElse(true))) {
			return super.extractItem(slot, amount, simulate);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void grow(int amount) {
		this.resizedList(amount);
		this.previousStacks = getResizedList(this.previousStacks.size() + amount, this.previousStacks);
	}

	@Override
	public void shrink(int amount) {
		this.resizedList(-amount);
		this.previousStacks = getResizedList(this.previousStacks.size() - amount, this.previousStacks);
	}

	@Override
	public CompoundTag serializeNBT() {
		return new CompoundTag();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {

	}

	private static NonNullList<ItemStack> getResizedList(int size, NonNullList<ItemStack> stacks) {
		NonNullList<ItemStack> newList = NonNullList.withSize(Math.max(0, size), ItemStack.EMPTY);
		for (int i = 0; i < newList.size() && i < stacks.size(); i++) {
			newList.set(i, stacks.get(i));
		}
		return newList;
	}

}
