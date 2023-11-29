package dev.xkmc.pandora.init.data;

import dev.xkmc.l2library.compat.curios.CurioEntityBuilder;
import dev.xkmc.l2library.compat.curios.CurioSlotBuilder;
import dev.xkmc.l2library.compat.curios.SlotCondition;
import dev.xkmc.l2library.serial.config.RecordDataProvider;
import dev.xkmc.pandora.init.Pandora;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class PandoraSlotGen extends RecordDataProvider {

	public static final String NAME = "pandora_charm";

	public PandoraSlotGen(DataGenerator generator) {
		super(generator, "Curios Generator");
	}

	@Override
	public void add(BiConsumer<String, Record> map) {
		map.accept("curios/curios/slots/" + NAME,
				new CurioSlotBuilder(1000000, new ResourceLocation(Pandora.MODID,
						"slot/empty_" + NAME).toString(),
						0, CurioSlotBuilder.Operation.SET,
						false, false, false, false,
						SlotCondition.of()
				));
		map.accept("curios/curios/entities/l2hostility_entity", new CurioEntityBuilder(
				new ArrayList<>(List.of(new ResourceLocation("player"))),
				new ArrayList<>(List.of("necklace", "bracelet", NAME)),
				SlotCondition.of()
		));
	}
}