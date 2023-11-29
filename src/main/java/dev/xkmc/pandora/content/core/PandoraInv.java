package dev.xkmc.pandora.content.core;

import net.minecraft.network.chat.Component;
import net.minecraftforge.items.IItemHandlerModifiable;

public record PandoraInv(PandoraInfo info, IItemHandlerModifiable handler) implements IPandoraInv {

	@Override
	public Component getTitle() {
		return info.name();
	}

	@Override
	public int getSlots() {
		return info.size();
	}

}
