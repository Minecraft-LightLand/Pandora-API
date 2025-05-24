package dev.xkmc.pandora.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.pandora.content.base.PandoraHolderCurio;
import dev.xkmc.pandora.content.base.PandoraItemHandler;
import dev.xkmc.pandora.event.PandoraSlotClickListener;
import dev.xkmc.pandora.init.data.*;
import dev.xkmc.pandora.init.registrate.PandoraItems;
import dev.xkmc.pandora.init.registrate.PandoraMenus;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosCapability;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Pandora.MODID)
@EventBusSubscriber(modid = Pandora.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Pandora {

	public static final String MODID = "pandora";
	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			MODID, 1
			//, e -> e.create(OpenPandoraPacket.class, PacketHandler.NetDir.PLAY_TO_SERVER)
	);
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Reg REG = new Reg(MODID);
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PandoraSlotClickListener CLICK = new PandoraSlotClickListener();

	public Pandora() {
		PandoraItems.register();
		PandoraMenus.register();
		PandoraConfig.init();
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {

		});
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.ItemHandler.ITEM,
				(stack, c) -> new PandoraItemHandler(stack, PandoraItems.DC_ITEMS.get()),
				PandoraItems.PANDORA_BRACELET.get(), PandoraItems.PANDORA_NECKLACE.get());

		event.registerItem(CuriosCapability.ITEM,
				(stack, c) -> new PandoraHolderCurio(stack),
				PandoraItems.PANDORA_BRACELET.get(), PandoraItems.PANDORA_NECKLACE.get());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.LANG, PandoraLangData::addTranslations);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, PandoraTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, PandoraMenus::genDataMap);

		boolean server = event.includeServer();
		var gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();

		gen.addProvider(server, new PandoraSlotGen(output, helper, pvd));
	}

}
