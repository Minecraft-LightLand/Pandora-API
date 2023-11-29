package dev.xkmc.pandora.content.menu;

import dev.xkmc.l2tabs.compat.BaseCuriosListScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.pandora.init.PandoraClient;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PandoraListScreen extends BaseCuriosListScreen<PandoraListMenu> {

	public PandoraListScreen(PandoraListMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	public void init() {
		super.init();
		new TabManager(this).init(this::addRenderableWidget, PandoraClient.TAB_PANDORA);
	}

}
