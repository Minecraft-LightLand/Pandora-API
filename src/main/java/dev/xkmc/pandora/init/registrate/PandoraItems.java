package dev.xkmc.pandora.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.pandora.content.base.PandoraHolder;
import dev.xkmc.pandora.init.Pandora;
import dev.xkmc.pandora.init.data.PandoraTagGen;
import net.minecraft.world.item.CreativeModeTab;

public class PandoraItems {


	public static final RegistryEntry<CreativeModeTab> TAB;
	public static final ItemEntry<PandoraHolder> PANDORA_NECKLACE, PANDORA_BRACELET;

	static {
		TAB = Pandora.REGISTRATE
				.buildL2CreativeTab("pandora", "Pandora",
						e -> e.icon(PandoraItems.PANDORA_NECKLACE::asStack));

		PANDORA_NECKLACE = Pandora.REGISTRATE.item("pandora_necklace", p ->
						new PandoraHolder(p.stacksTo(1).fireResistant(), 18))
				.tag(PandoraTagGen.NECKLACE).register();

		PANDORA_BRACELET = Pandora.REGISTRATE.item("pandora_bracelet", p ->
						new PandoraHolder(p.stacksTo(1).fireResistant(), 9))
				.tag(PandoraTagGen.BRACELET).register();
	}

	public static void register() {

	}

}
