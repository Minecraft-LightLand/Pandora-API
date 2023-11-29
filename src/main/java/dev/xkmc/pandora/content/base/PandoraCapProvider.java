package dev.xkmc.pandora.content.base;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosCapability;

public final class PandoraCapProvider implements ICapabilityProvider {

	private final LazyOptional<BasePandoraInvWrapper> inv;
	private final LazyOptional<PandoraCurioCap> curio;

	public PandoraCapProvider(IPandoraHolder item, ItemStack stack) {
		inv = LazyOptional.of(() -> new BasePandoraInvWrapper(stack));
		curio = LazyOptional.of(() -> new PandoraCurioCap(item, stack));
	}

	@Override
	@NotNull
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return inv.cast();
		}
		if (cap == CuriosCapability.INVENTORY) {
			return curio.cast();
		}
		return LazyOptional.empty();
	}

}
