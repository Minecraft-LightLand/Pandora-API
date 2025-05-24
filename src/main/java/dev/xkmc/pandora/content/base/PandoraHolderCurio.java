package dev.xkmc.pandora.content.base;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.ArrayList;
import java.util.List;

public record PandoraHolderCurio(ItemStack stack) implements ICurio {

	@Override
	public ItemStack getStack() {
		return stack;
	}

	public @Nullable ICurio[] getItems() {
		return IPandoraHolder.getCurios(stack);
	}

	@Override
	public List<Component> getAttributesTooltip(List<Component> tooltips, Item.TooltipContext context) {
		return new ArrayList<>();//TODO
	}

	@Override
	public void curioTick(SlotContext ctx) {
		for (var e : getItems()) {
			if (e == null) continue;
			e.curioTick(ctx);
		}
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext ctx, ResourceLocation id) {
		Multimap<Holder<Attribute>, AttributeModifier> ans = LinkedHashMultimap.create();
		var arr = getItems();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == null) continue;
			ans.putAll(arr[i].getAttributeModifiers(ctx, id.withSuffix("/" + i)));
		}
		return ans;
	}

	@Override
	public int getFortuneLevel(SlotContext ctx, @Nullable LootContext lootContext) {
		int ans = 0;
		for (var e : getItems()) {
			if (e == null) continue;
			ans += e.getFortuneLevel(ctx, lootContext);
		}
		return ans;
	}

	@Override
	public int getLootingLevel(SlotContext ctx, @Nullable LootContext lootContext) {
		int ans = 0;
		for (var e : getItems()) {
			if (e == null) continue;
			ans += e.getLootingLevel(ctx, lootContext);
		}
		return ans;
	}

	@Override
	public boolean makesPiglinsNeutral(SlotContext ctx) {
		for (var e : getItems()) {
			if (e == null) continue;
			if (e.makesPiglinsNeutral(ctx))
				return true;
		}
		return false;
	}

	@Override
	public boolean canWalkOnPowderedSnow(SlotContext ctx) {
		for (var e : getItems()) {
			if (e == null) continue;
			if (e.canWalkOnPowderedSnow(ctx))
				return true;
		}
		return false;
	}

	@Override
	public boolean isEnderMask(SlotContext ctx, EnderMan enderMan) {
		for (var e : getItems()) {
			if (e == null) continue;
			if (e.isEnderMask(ctx, enderMan))
				return true;
		}
		return false;
	}

}
