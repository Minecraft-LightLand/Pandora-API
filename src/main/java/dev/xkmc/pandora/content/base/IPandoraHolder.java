package dev.xkmc.pandora.content.base;

import dev.xkmc.pandora.content.core.IPandoraInvGetter;
import dev.xkmc.pandora.content.core.PandoraInfo;
import dev.xkmc.pandora.content.core.PandoraInvGetter;
import dev.xkmc.pandora.init.data.PandoraTagGen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IPandoraHolder {

	static ListTag getListTag(ItemStack stack) {
		var tag = stack.getOrCreateTag();
		if (tag.contains("PandoraCharms")) {
			return stack.getOrCreateTag().getList("PandoraCharms", Tag.TAG_COMPOUND);
		} else {
			return new ListTag();
		}
	}

	static void setListTag(ItemStack stack, ListTag list) {
		stack.getOrCreateTag().put("PandoraCharms", list);
	}

	static List<ItemStack> getItems(ItemStack stack) {
		List<ItemStack> ans = new ArrayList<>();
		ListTag tag = getListTag(stack);
		for (Tag value : tag) {
			if (value instanceof CompoundTag ctag) {
				ItemStack i = ItemStack.of(ctag);
				ans.add(i);
			}
		}
		if (!ans.isEmpty()) {
			int size = ((IPandoraHolder) stack.getItem()).getSlots(stack);
			while (ans.size() < size) {
				ans.add(ItemStack.EMPTY);
			}
		}
		return ans;
	}

	static void setItems(ItemStack stack, List<ItemStack> list) {
		ListTag tag = new ListTag();
		for (int i = 0; i < list.size(); i++) {
			tag.add(i, list.get(i).save(new CompoundTag()));
		}
		IPandoraHolder.setListTag(stack, tag);
	}

	default IPandoraInvGetter getSupplier(String key, int i, ItemStack stack) {
		return new PandoraInvGetter(key, i, new PandoraInfo(stack.getHoverName(), getSlots(stack)));
	}

	int getSlots(ItemStack stack);

	default boolean isItemValid(int slot, ItemStack stack) {
		return stack.is(PandoraTagGen.PANDORA_SLOT);
	}

	default Optional<IItemHandlerModifiable> getCap(ItemStack stack) {
		return stack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().map(e -> e instanceof IItemHandlerModifiable mod ? mod : null);
	}

}
