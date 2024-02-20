package dev.xkmc.pandora.mixin;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.pandora.content.core.PandoraCurioStacksHandler;
import dev.xkmc.pandora.init.data.PandoraSlotGen;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.common.capability.CurioInventoryCapability;

import java.util.Map;

@Mixin(CurioInventoryCapability.CurioInventoryWrapper.class)
public class CurioInventoryWrapperMixin {

	@Shadow(remap = false)
	Map<String, ICurioStacksHandler> curios;

	@Shadow(remap = false)
	LivingEntity wearer;

	@Inject(at = @At("TAIL"), method = {"reset", "setCurios"}, remap = false)
	public void pandora$reset$usePandoraHandler(CallbackInfo ci) {
		if (wearer == null) return;
		String id = PandoraSlotGen.NAME;
		var e = curios.get(id);
		if (e == null) return;
		ICuriosItemHandler self = Wrappers.cast(this);
		curios.put(id, new PandoraCurioStacksHandler(self, id, e.getDropRule()).from(e));
	}

}
