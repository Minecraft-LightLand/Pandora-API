package dev.xkmc.pandora.init;

import dev.xkmc.pandora.content.base.InvClientTooltip;
import dev.xkmc.pandora.content.base.InvTooltip;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = Pandora.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PandoraClient {

	@SubscribeEvent
	public static void client(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {

		});
	}

	@SubscribeEvent
	public static void registerClientTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(InvTooltip.class, InvClientTooltip::new);
	}

}
