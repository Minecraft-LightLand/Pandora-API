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

import java.util.List;
import java.util.function.Predicate;

@Mixin(ICuriosItemHandler.class)
public interface ICuriosItemHandlerMixin {

	@Shadow
	List<SlotResult> findCurios(Predicate<ItemStack> predicate);

	@Inject(method = "isEquipped(Lnet/minecraft/world/item/Item;)Z", cancellable = true, at = @At("HEAD"))
	default void pandora$isEquipped(Item item, CallbackInfoReturnable<Boolean> cir) {
		if (item.builtInRegistryHolder().is(PandoraTagGen.PANDORA_SLOT)) {
			var list = findCurios(i -> i.getItem() instanceof IPandoraHolder);
			for (var e : list) {
				if (e.stack().is(item)) {
					cir.setReturnValue(true);
				}
			}
		}
	}

}
