package dev.xkmc.pandora.content.base;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record InvTooltip(IPandoraHolder item, ItemStack stack) implements TooltipComponent {

	public static Optional<TooltipComponent> get(IPandoraHolder item, ItemStack stack) {
		if (Screen.hasShiftDown()) {
			return Optional.empty();
		}
		var list = IPandoraHolder.getListTag(stack);
		if (!list.isEmpty()) {
			return Optional.of(new InvTooltip(item, stack));
		}
		return Optional.empty();
	}

}
