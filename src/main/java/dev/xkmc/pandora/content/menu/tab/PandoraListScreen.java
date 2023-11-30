package dev.xkmc.pandora.content.menu.tab;

import dev.xkmc.l2tabs.compat.BaseCuriosListScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.pandora.init.PandoraClient;
import net.minecraft.client.gui.GuiGraphics;
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

	@Override
	protected void renderLabels(GuiGraphics g, int mx, int my) {
		super.renderLabels(g, mx, my);
		if (menu.curios instanceof PandoraWrapper pandora) {
			for (var e : pandora.titles) {
				drawLeft(g, e.text(), 18 + e.row() * 18);
			}
		}
	}

	private void drawLeft(GuiGraphics g, Component comp, int y) {
		int x = titleLabelX;
		y += titleLabelY;
		g.drawString(font, comp, x, y, 4210752, false);
	}

}
