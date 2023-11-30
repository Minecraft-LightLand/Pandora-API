package dev.xkmc.pandora.event;

import dev.xkmc.l2screentracker.click.writable.ClickedPlayerSlotResult;
import dev.xkmc.l2screentracker.click.writable.WritableStackClickHandler;
import dev.xkmc.l2screentracker.screen.base.ScreenTracker;
import dev.xkmc.pandora.content.base.IPandoraHolder;
import dev.xkmc.pandora.init.Pandora;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class PandoraSlotClickListener extends WritableStackClickHandler {

	public PandoraSlotClickListener() {
		super(new ResourceLocation(Pandora.MODID, "pandora"));
	}

	@Override
	public boolean isAllowed(ItemStack stack) {
		return stack.getItem() instanceof IPandoraHolder;
	}

	@Override
	protected void handle(ServerPlayer player, ClickedPlayerSlotResult result) {
		ItemStack carried = player.containerMenu.getCarried();
		if (!carried.isEmpty()) return;
		if (result.stack().getItem() instanceof IPandoraHolder bag) {
			ScreenTracker.onServerOpen(player);
			bag.open(player, result.slot(), result.stack());
		}
	}

}
