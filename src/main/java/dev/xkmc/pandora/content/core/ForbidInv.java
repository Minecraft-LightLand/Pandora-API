package dev.xkmc.pandora.content.core;

import dev.xkmc.pandora.init.data.PandoraLangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public record ForbidInv(ForbiddenEmptyHandler handler) implements IPandoraInv {

	@Override
	public int getSlots() {
		return 0;
	}

	@Override
	public Component getTitle() {
		return PandoraLangData.INVALID.get().withStyle(ChatFormatting.RED);
	}

}
