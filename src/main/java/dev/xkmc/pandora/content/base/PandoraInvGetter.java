package dev.xkmc.pandora.content.base;

import dev.xkmc.pandora.content.core.ForbiddenEmptyHandler;
import dev.xkmc.pandora.content.core.IPandoraInvGetter;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public record PandoraInvGetter(String id, int slot, int size) implements IPandoraInvGetter {

	@Override
	public IItemHandlerModifiable get(ICuriosItemHandler curios) {
		var opt = curios.getStacksHandler(id);
		if (opt.isPresent()) {
			var inv = opt.get().getStacks();
			if (inv.getSlots() > slot) {
				ItemStack stack = inv.getStackInSlot(slot);
				if (stack.getItem() instanceof IPandoraHolder holder) {
					var ans = holder.getCap(stack);
					if (ans.isPresent()) {
						return ans.get();
					}
				}
			}
		}
		return new ForbiddenEmptyHandler(size);
	}

}
