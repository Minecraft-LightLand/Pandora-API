package dev.xkmc.pandora.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.pandora.init.data.PandoraLangData;
import dev.xkmc.pandora.init.data.PandoraSlotGen;
import dev.xkmc.pandora.init.data.PandoraTagGen;
import dev.xkmc.pandora.init.data.RecipeGen;
import dev.xkmc.pandora.init.registrate.PandoraItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Pandora.MODID)
@Mod.EventBusSubscriber(modid = Pandora.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Pandora {

	public static final String MODID = "pandora";
	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			new ResourceLocation(MODID, "main"), 1
	);
	public static final Logger LOGGER = LogManager.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public Pandora() {
		PandoraItems.register();

		REGISTRATE.addDataGenerator(ProviderType.LANG, PandoraLangData::addTranslations);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, PandoraTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {

		});
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		boolean server = event.includeServer();
		PackOutput output = event.getGenerator().getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		var gen = event.getGenerator();
		gen.addProvider(server, new PandoraSlotGen(gen));
	}

}
