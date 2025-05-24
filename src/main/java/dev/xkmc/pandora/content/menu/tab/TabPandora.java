//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.xkmc.pandora.content.menu.tab;

import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.network.chat.Component;

public class TabPandora extends TabBase<InvTabData, TabPandora> {

	public TabPandora(int index, TabToken<InvTabData, TabPandora> token, TabManager<InvTabData> manager, Component title) {
		super(index, token, manager, title);
	}

	public void onTabClicked() {
		//Pandora.HANDLER.toServer(new OpenPandoraPacket(OpenPandoraHandler.CURIO_OPEN));
	}

}
