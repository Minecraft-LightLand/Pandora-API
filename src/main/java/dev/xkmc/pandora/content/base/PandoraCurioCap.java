package dev.xkmc.pandora.content.base;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.pandora.init.data.PandoraSlotGen;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.UUID;

public record PandoraCurioCap(IPandoraHolder item, ItemStack stack) implements ICurio {

	@Override
	public ItemStack getStack() {
		return stack;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
		Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
		CuriosApi.addSlotModifier(map, PandoraSlotGen.NAME, uuid, item.getSlots(stack), AttributeModifier.Operation.ADDITION);
		return map;
	}

}
