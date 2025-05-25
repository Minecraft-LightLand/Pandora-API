package dev.xkmc.pandora.mixin;

import dev.xkmc.pandora.content.base.IPandoraHolder;
import dev.xkmc.pandora.init.data.PandoraTagGen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ICuriosItemHandler.class)
public interface ICuriosItemHandlerMixin {

	@Shadow
	Optional<SlotResult> findFirstCurio(Predicate<ItemStack> predicate, String s);

	@Inject(method = "isEquipped(Lnet/minecraft/world/item/Item;)Z", cancellable = true, at = @At("HEAD"))
	default void pandora$isEquipped(Item item, CallbackInfoReturnable<Boolean> cir) {
		var holder = item.builtInRegistryHolder();
		if (holder.is(PandoraTagGen.PANDORA_SLOT)) {
			var id = "pandora:" + holder.unwrapKey().orElseThrow().location();
			if (findFirstCurio(stack -> IPandoraHolder.findInside(stack, item), id).isPresent()) {
				cir.setReturnValue(true);
			}
		}
	}

}
