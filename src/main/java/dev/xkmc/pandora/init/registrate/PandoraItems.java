package dev.xkmc.pandora.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.pandora.content.base.PandoraHolder;
import dev.xkmc.pandora.init.Pandora;
import dev.xkmc.pandora.init.data.PandoraTagGen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.component.ItemContainerContents;

public class PandoraItems {

	public static final SimpleEntry<CreativeModeTab> TAB;
	public static final ItemEntry<PandoraHolder> PANDORA_NECKLACE, PANDORA_BRACELET;

	private static final DCReg DC = DCReg.of(Pandora.REG);
	public static final DCVal<ItemContainerContents> DC_ITEMS;

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

		DC_ITEMS = DC.reg("backpack_content", ItemContainerContents.CODEC, ItemContainerContents.STREAM_CODEC, true);
	}

	public static void register() {

	}

}
