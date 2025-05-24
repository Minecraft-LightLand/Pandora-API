package dev.xkmc.pandora.init.data;

import dev.xkmc.pandora.init.Pandora;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class PandoraSlotGen extends CuriosDataProvider {

	public static final String NAME = "pandora";

	public PandoraSlotGen(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
		super(Pandora.MODID, output, fileHelper, registries);
	}

	@Override
	public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
		createSlot(NAME).size(0).order(1000943)
				.addCosmetic(false).useNativeGui(false).renderToggle(false)
				.icon(ResourceLocation.fromNamespaceAndPath(Pandora.MODID, "slot/empty_" + NAME));
		createEntities("player").addEntities(EntityType.PLAYER)
				.addSlots("necklace", "bracelet", NAME);
	}

}