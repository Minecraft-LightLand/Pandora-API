package dev.xkmc.pandora.mixin;

import dev.xkmc.pandora.init.data.PandoraSlotGen;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncModifiers;

import java.util.Map;

@Mixin(SPacketSyncModifiers.class)
public class SPacketSyncModifiersMixin {

	@Inject(at = @At("TAIL"), method = "<init>(Ljava/util/Map;I)V", remap = false)
	public void pandora$handle$changeOrder(Map<String, CompoundTag> map, int entityId, CallbackInfo ci) {
		if (map.containsKey(PandoraSlotGen.NAME)) {
			var x = map.remove(PandoraSlotGen.NAME);
			map.put(PandoraSlotGen.NAME, x);
		}
	}

}
