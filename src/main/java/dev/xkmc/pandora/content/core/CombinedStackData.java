package dev.xkmc.pandora.content.core;

import dev.xkmc.pandora.content.base.IPandoraHolder;
import dev.xkmc.pandora.init.data.PandoraSlotGen;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.EmptyHandler;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;

public class CombinedStackData {

	private final ICuriosItemHandler parent;
	private final boolean isCosmetic;

	private List<IPandoraInvGetter> itemHandler;
	private int[] baseIndex;
	private int[] forwardMap;
	private int slotCount;

	public CombinedStackData(ICuriosItemHandler parent, boolean isCosmetic) {
		this.parent = parent;
		this.isCosmetic = isCosmetic;
	}

	private void aggregateCurios(int actualSize) {
		int totalSize = 0;
		List<IPandoraInvGetter> ans = new ArrayList<>();
		if (!isCosmetic) {
			for (var e : parent.getCurios().entrySet()) {
				if (e.getKey().equals(PandoraSlotGen.NAME)) continue;
				var inv = e.getValue().getStacks();
				for (int i = 0; i < inv.getSlots(); i++) {
					ItemStack stack = inv.getStackInSlot(i);
					if (stack.getItem() instanceof IPandoraHolder holder) {
						IPandoraInvGetter sup = holder.getSupplier(e.getKey(), i, stack);
						ans.add(sup);
						totalSize += sup.get(parent).handler().getSlots();
					}
				}
			}
		}
		if (actualSize < totalSize) {
			var forbid = new ForbiddenEmptyHandler(totalSize - actualSize);
			ans.add(c -> new ForbidInv(forbid));
		}
		itemHandler = ans;
	}

	protected void buildStackData(int actualSize) {
		aggregateCurios(actualSize);
		baseIndex = new int[itemHandler.size()];
		int index = 0;
		for (int i = 0; i < baseIndex.length; i++) {
			int size = itemHandler.get(i).get(parent).handler().getSlots();
			index += size;
			baseIndex[i] = index;
		}
		slotCount = index;
		forwardMap = new int[slotCount];
		int start = 0;
		for (int i = 0; i < baseIndex.length; i++) {
			for (int j = start; j < baseIndex[i]; j++) {
				forwardMap[j] = i;
			}
			start = baseIndex[i];
		}
	}

	// returns the handler index for the slot
	protected int getIndexForSlot(int slot) {
		if (slot < 0 || slot >= slotCount)
			return -1;
		return forwardMap[slot];
	}

	protected IItemHandlerModifiable getHandlerFromIndex(int index) {
		if (index < 0 || index >= itemHandler.size()) {
			return (IItemHandlerModifiable) EmptyHandler.INSTANCE;
		}
		return itemHandler.get(index).get(parent).handler();
	}

	protected int getSlotFromIndex(int slot, int index) {
		if (index <= 0 || index >= baseIndex.length) {
			return slot;
		}
		return slot - baseIndex[index - 1];
	}

	protected int getSlotCount() {
		return slotCount;
	}

	public List<IPandoraInv> getSplitSlots() {
		return itemHandler.stream().map(e -> e.get(parent)).toList();
	}

}
