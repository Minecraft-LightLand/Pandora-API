package dev.xkmc.pandora.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.pandora.init.registrate.PandoraItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.BiFunction;

public class RecipeGen {

	private static final String currentFolder = "";

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PandoraItems.PANDORA_NECKLACE.get())::unlockedBy, Items.CHAIN)
				.pattern("CCC").pattern("C C").pattern("CAC")
				.define('C', Items.CHAIN)
				.define('A', Items.GOLD_INGOT)
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PandoraItems.PANDORA_BRACELET.get())::unlockedBy, Items.CHAIN)
				.pattern(" C ").pattern("C C").pattern(" A ")
				.define('C', Items.CHAIN)
				.define('A', Items.GOLD_INGOT)
				.save(pvd);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
