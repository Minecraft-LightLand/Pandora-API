package dev.xkmc.pandora.content.core;

import net.minecraft.network.chat.Component;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IPandoraInv {

	Component getTitle();

	int getSlots();

	IItemHandlerModifiable handler();

}
