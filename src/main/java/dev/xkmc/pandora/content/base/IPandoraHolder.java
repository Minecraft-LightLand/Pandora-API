package dev.xkmc.pandora.content.base;

import dev.xkmc.l2menustacker.click.writable.ContainerCallback;
import dev.xkmc.l2menustacker.screen.source.PlayerSlot;
import dev.xkmc.pandora.init.data.PandoraTagGen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IPandoraHolder {

	static List<ItemStack> getItems(ItemStack stack) {
		if (!(stack.getItem() instanceof IPandoraHolder pandora))
			return List.of();
		var handler = pandora.getCap(stack);
		if (handler.isEmpty()) return List.of();
		List<ItemStack> list = new ArrayList<>();
		for (int i = 0; i < handler.get().getSlots(); i++) {
			list.add(handler.get().getStackInSlot(i));
		}
		return list;
	}

	static ICurio[] getCurios(ItemStack stack) {
		if (!(stack.getItem() instanceof IPandoraHolder pandora))
			return new ICurio[0];
		var handler = pandora.getCap(stack);
		if (handler.isEmpty()) return new ICurio[0];
		int n = handler.get().getSlots();
		ICurio[] ans = new ICurio[n];
		for (int i = 0; i < n; i++) {
			ans[i] = CuriosApi.getCurio(handler.get().getStackInSlot(i)).orElse(null);
		}
		return ans;
	}

	static boolean isEmpty(ItemStack stack) {
		if (!(stack.getItem() instanceof IPandoraHolder pandora))
			return true;
		var handler = pandora.getCap(stack);
		if (handler.isEmpty()) return true;
		for (int i = 0; i < handler.get().getSlots(); i++) {
			if (!handler.get().getStackInSlot(i).isEmpty())
				return false;
		}
		return true;
	}

	static boolean findInside(ItemStack stack, Item item) {
		if (!(stack.getItem() instanceof IPandoraHolder pandora))
			return false;
		var handler = pandora.getCap(stack);
		if (handler.isEmpty()) return false;
		for (int i = 0; i < handler.get().getSlots(); i++) {
			if (handler.get().getStackInSlot(i).is(item))
				return true;
		}
		return false;
	}

	default Optional<IItemHandlerModifiable> getCap(ItemStack stack) {
		var ans = stack.getCapability(Capabilities.ItemHandler.ITEM);
		if (ans instanceof IItemHandlerModifiable mod)
			return Optional.of(mod);
		return Optional.empty();
	}

	int getSlots(ItemStack stack);

	void open(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack, @Nullable ContainerCallback container);

	default boolean isItemValid(int slot, ItemStack stack) {
		return stack.is(PandoraTagGen.PANDORA_SLOT);
	}

}
