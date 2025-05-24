package dev.xkmc.pandora.content.menu.edit;

import net.minecraft.world.Container;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class PandoraInvWrapper extends InvWrapper {

	public PandoraInvWrapper(Container inv) {
		super(inv);
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

}
