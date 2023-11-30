package dev.xkmc.pandora.content.menu.edit;

import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PandoraEditScreen extends BaseContainerScreen<PandoraEditMenu> {

	public PandoraEditScreen(PandoraEditMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	protected void renderBg(GuiGraphics g, float pt, int mx, int my) {
		var sm = menu.sprite.get();
		var sr = sm.getRenderer(this);
		sr.start(g);
		for (int i = 0; i < this.menu.handler.getSlots(); ++i) {
			sr.draw(g, "grid", "slot", i % 9 * 18 - 1, i / 9 * 18 - 1);
		}
	}

}
