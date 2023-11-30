package dev.xkmc.pandora.content.menu.edit;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class PandoraSlot extends SlotItemHandler {

	private final IItemHandlerModifiable handler;
	private final int index;

	public PandoraSlot(IItemHandlerModifiable itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		handler = itemHandler;
		this.index = index;
	}

	@Override
	public void setChanged() {
		handler.setStackInSlot(index, getItem());
	}
}
