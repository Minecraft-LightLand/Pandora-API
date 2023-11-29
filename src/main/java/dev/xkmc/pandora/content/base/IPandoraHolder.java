package dev.xkmc.pandora.content.base;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.function.Supplier;

public interface IPandoraHolder {
	Supplier<IItemHandlerModifiable> getSupplier(String key, int i, ItemStack stack);
}
