package dev.xkmc.pandora.init;

import dev.xkmc.l2tabs.tabs.core.TabRegistry;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.pandora.content.menu.TabPandora;
import dev.xkmc.pandora.init.data.PandoraLangData;
import dev.xkmc.pandora.init.registrate.PandoraItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Pandora.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PandoraClient {

	public static TabToken<TabPandora> TAB_PANDORA;

	@SubscribeEvent
	public static void client(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			TAB_PANDORA = TabRegistry.registerTab(2010, TabPandora::new, PandoraItems.PANDORA_NECKLACE::get, PandoraLangData.TITLE.get());
		});
	}

	@SubscribeEvent
	public static void onResourceReload(RegisterClientReloadListenersEvent event) {
	}

	@SubscribeEvent
	public static void registerOverlay(RegisterGuiOverlaysEvent event) {
	}

}
