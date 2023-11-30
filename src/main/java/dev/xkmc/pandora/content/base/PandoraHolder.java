package dev.xkmc.pandora.content.base;

import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import dev.xkmc.pandora.content.menu.edit.PandoraEditPvd;
import dev.xkmc.pandora.init.data.PandoraLangData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PandoraHolder extends Item implements IPandoraHolder {

	private final int size;

	public PandoraHolder(Properties props, int size) {
		super(props);
		this.size = size;
	}

	@Override
	public int getSlots(ItemStack stack) {
		return size;
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return InvTooltip.get(this, stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(PandoraLangData.TOOLTIP_HOLDER.get(getSlots(stack)).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new BasePandoraInvWrapper(stack);
	}

	@Override
	public void open(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack) {
		new PandoraEditPvd(player, slot, stack).open();
	}

}
