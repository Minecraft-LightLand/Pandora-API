package dev.xkmc.pandora.content.menu.tab;

import dev.xkmc.l2tabs.compat.CuriosEventHandler;
import dev.xkmc.pandora.init.data.PandoraLangData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkHooks;

public record PandoraMenuPvd(MenuType<PandoraListMenu> type, int page) implements MenuProvider {

	public PandoraMenuPvd(MenuType<PandoraListMenu> type) {
		this(type, 0);
	}

	@Override
	public Component getDisplayName() {
		return PandoraLangData.TITLE.get();
	}

	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
		return new PandoraListMenu(type, wid, inv, new PandoraWrapper(player, page));
	}

	public void writeBuf(FriendlyByteBuf buf) {
		buf.writeInt(page);
	}

	public void open(ServerPlayer player) {
		CuriosEventHandler.openMenuWrapped(player, () ->
				NetworkHooks.openScreen(player, this, this::writeBuf));
	}

}
