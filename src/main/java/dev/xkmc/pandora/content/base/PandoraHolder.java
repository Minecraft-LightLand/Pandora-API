package dev.xkmc.pandora.content.base;

import dev.xkmc.l2menustacker.click.writable.ContainerCallback;
import dev.xkmc.l2menustacker.screen.source.PlayerSlot;
import dev.xkmc.pandora.content.menu.edit.PandoraEditPvd;
import dev.xkmc.pandora.init.data.PandoraLangData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PandoraHolder extends Item implements IPandoraHolder {

	private final int size;

	public PandoraHolder(Properties prop, int size) {
		super(prop);
		this.size = size;
	}

	@Override
	public int getSlots(ItemStack stack) {
		return size;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player instanceof ServerPlayer sp) {
			int slot = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 40;
			open(sp, PlayerSlot.ofInventory(slot), stack, null);
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return InvTooltip.get(this, stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		list.add(PandoraLangData.TOOLTIP_HOLDER.get(getSlots(stack)).withStyle(ChatFormatting.GRAY));
		if (!Screen.hasShiftDown()) {
			if (!IPandoraHolder.isEmpty(stack)) {
				int count = 0;
				for (var e : IPandoraHolder.getItems(stack)) {
					if (!e.isEmpty()) count++;
				}
				if (count > 0) {
					list.add(PandoraLangData.TOOLTIP_CONTAIN.get(count).withStyle(ChatFormatting.DARK_AQUA));
				}
			}
			list.add(PandoraLangData.TOOLTIP_SHIFT.get().withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public void open(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack, @Nullable ContainerCallback container) {
		new PandoraEditPvd(player, slot, stack, container).open();
	}

}
