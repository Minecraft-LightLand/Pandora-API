package dev.xkmc.pandora.content.menu.tab;

import dev.xkmc.l2tabs.compat.BaseCuriosListMenu;
import dev.xkmc.l2tabs.compat.TabCurioSlot;
import dev.xkmc.pandora.init.registrate.PandoraMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class PandoraListMenu extends BaseCuriosListMenu<PandoraListMenu> {
	public static PandoraListMenu fromNetwork(MenuType<PandoraListMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int page = buf.readInt();
		return new PandoraListMenu(type, wid, plInv, new PandoraWrapper(plInv.player, page));
	}

	protected PandoraListMenu(MenuType<?> type, int wid, Inventory plInv, PandoraWrapper curios) {
		super(type, wid, plInv, curios);
	}

	@Override
	public void switchPage(ServerPlayer sp, int page) {
		new PandoraMenuPvd(PandoraMenus.LIST_MENU.get(), page).open(sp);
	}
}
