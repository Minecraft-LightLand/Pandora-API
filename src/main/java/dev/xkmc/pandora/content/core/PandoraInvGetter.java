package dev.xkmc.pandora.content.core;

import dev.xkmc.pandora.content.base.IPandoraHolder;
import dev.xkmc.pandora.content.core.*;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public record PandoraInvGetter(String id, int slot, PandoraInfo info) implements IPandoraInvGetter {

	@Override
	public IPandoraInv get(ICuriosItemHandler curios) {
		var opt = curios.getStacksHandler(id);
		if (opt.isPresent()) {
			var inv = opt.get().getStacks();
			if (inv.getSlots() > slot) {
				ItemStack stack = inv.getStackInSlot(slot);
				if (stack.getItem() instanceof IPandoraHolder holder) {
					var ans = holder.getCap(stack);
					if (ans.isPresent()) {
						return new PandoraInv(info, ans.get());
					}
				}
			}
		}
		return new ForbidInv(new ForbiddenEmptyHandler(info.size()));
	}

}
