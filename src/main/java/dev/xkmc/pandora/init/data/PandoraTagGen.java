package dev.xkmc.pandora.init.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import dev.xkmc.pandora.init.Pandora;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class PandoraTagGen {

	public static final TagKey<Item> PANDORA_SLOT = ItemTags.create(new ResourceLocation("curios", "pandora_charm"));

	public static final TagKey<Item> ALLOW_DUPLICATE = ItemTags.create(new ResourceLocation(Pandora.MODID, "allow_duplicates"));
	public static final TagKey<Item> NECKLACE = ItemTags.create(new ResourceLocation("curios", "necklace"));
	public static final TagKey<Item> BRACELET = ItemTags.create(new ResourceLocation("curios", "bracelet"));

	public static final TagKey<Item> NO_SEAL = ItemTags.create(new ResourceLocation("l2hostility", "no_seal"));

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(ALLOW_DUPLICATE);
		pvd.addTag(PANDORA_SLOT);
		pvd.addTag(NO_SEAL).addTag(PANDORA_SLOT);
	}

}
