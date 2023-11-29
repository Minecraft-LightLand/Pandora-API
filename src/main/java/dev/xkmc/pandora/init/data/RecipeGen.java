package dev.xkmc.pandora.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.world.item.Item;

import java.util.function.BiFunction;

public class RecipeGen {

	private static final String currentFolder = "";

	public static void genRecipe(RegistrateRecipeProvider pvd) {
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
