//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.xkmc.pandora.content.menu.tab;

import dev.xkmc.l2tabs.tabs.core.BaseTab;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.pandora.init.Pandora;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class TabPandora extends BaseTab<TabPandora> {

	public TabPandora(TabToken<TabPandora> token, TabManager manager, ItemStack stack, Component title) {
		super(token, manager, stack, title);
	}

	public void onTabClicked() {
		Pandora.HANDLER.toServer(new OpenPandoraPacket(OpenPandoraHandler.CURIO_OPEN));
	}

}
