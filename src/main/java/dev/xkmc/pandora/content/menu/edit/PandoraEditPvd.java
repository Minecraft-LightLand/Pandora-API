package dev.xkmc.pandora.content.menu.edit;

import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.pandora.content.base.IPandoraHolder;
import dev.xkmc.pandora.init.registrate.PandoraMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

public final class PandoraEditPvd implements MenuProvider {

	private final ServerPlayer player;
	private final PlayerSlot<?> slot;
	private final ItemStack stack;
	private final IPandoraHolder bag;

	public PandoraEditPvd(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack) {
		this.player = player;
		this.slot = slot;
		this.stack = stack;
		bag = Wrappers.cast(stack.getItem());
	}

	@Override
	public Component getDisplayName() {
		return stack.getHoverName();
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new PandoraEditMenu(PandoraMenus.EDIT_MENU.get(), id, inventory, slot, bag.getSlots(stack));
	}

	public void writeBuffer(FriendlyByteBuf buf) {
		slot.write(buf);
		buf.writeInt(bag.getSlots(stack));
	}

	public void open() {
		NetworkHooks.openScreen(player, this, this::writeBuffer);
	}

}
