package dev.xkmc.pandora.init.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class PandoraTagGen {

	public static final TagKey<Item> PANDORA_SLOT = ItemTags.create(new ResourceLocation("curios", "pandora_charm"));
	public static final TagKey<Item> NO_SEAL = ItemTags.create(new ResourceLocation("l2hostility", "no_seal"));

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(NO_SEAL).addTag(PANDORA_SLOT);
	}

}
