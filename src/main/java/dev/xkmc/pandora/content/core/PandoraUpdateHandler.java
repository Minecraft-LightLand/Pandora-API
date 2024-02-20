package dev.xkmc.pandora.content.core;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncModifiers;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncStack;
import top.theillusivec4.curios.common.util.EquipCurioTrigger;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PandoraUpdateHandler {

	public static void update(
			LivingEntity livingEntity,
			ICuriosItemHandler handler,
			PandoraCurioStacksHandler stacksHandler,
			String identifier
	) {
		IDynamicStackHandler stackHandler = stacksHandler.getStacks();

		for (int i = 0; i < stacksHandler.getSlots(); i++) {
			NonNullList<Boolean> renderStates = stacksHandler.getRenders();
			SlotContext slotContext = new SlotContext(identifier, livingEntity, i, false,
					renderStates.size() > i && renderStates.get(i));
			ItemStack stack = stackHandler.getStackInSlot(i);
			LazyOptional<ICurio> currentCurio = CuriosApi.getCurio(stack);
			if (!livingEntity.level().isClientSide) {
				ItemStack prevStack = stackHandler.getPreviousStackInSlot(i);
				if (!ItemStack.matches(stack, prevStack)) {
					LazyOptional<ICurio> prevCurio = CuriosApi.getCurio(prevStack);
					syncCurios(livingEntity, stack, currentCurio, prevCurio, identifier, i, false,
							renderStates.size() > i && renderStates.get(i), SPacketSyncStack.HandlerType.EQUIPMENT);
					MinecraftForge.EVENT_BUS
							.post(new CurioChangeEvent(livingEntity, identifier, i, prevStack, stack));
					UUID uuid = UUID.nameUUIDFromBytes((identifier + i).getBytes());

					if (!prevStack.isEmpty()) {
						Multimap<Attribute, AttributeModifier> map =
								CuriosApi.getAttributeModifiers(slotContext, uuid, prevStack);
						Multimap<String, AttributeModifier> slots = HashMultimap.create();
						Set<SlotAttribute> toRemove = new HashSet<>();

						for (Attribute attribute : map.keySet()) {

							if (attribute instanceof SlotAttribute wrapper) {
								slots.putAll(wrapper.getIdentifier(), map.get(attribute));
								toRemove.add(wrapper);
							}
						}

						for (Attribute attribute : toRemove) {
							map.removeAll(attribute);
						}
						livingEntity.getAttributes().removeAttributeModifiers(map);
						handler.removeSlotModifiers(slots);
						prevCurio.ifPresent(curio -> curio.onUnequip(slotContext, stack));
					}

					if (!stack.isEmpty()) {
						Multimap<Attribute, AttributeModifier> map =
								CuriosApi.getAttributeModifiers(slotContext, uuid, stack);
						Multimap<String, AttributeModifier> slots = HashMultimap.create();
						Set<SlotAttribute> toRemove = new HashSet<>();
						for (Attribute attribute : map.keySet()) {
							if (attribute instanceof SlotAttribute wrapper) {
								slots.putAll(wrapper.getIdentifier(), map.get(attribute));
								toRemove.add(wrapper);
							}
						}

						for (Attribute attribute : toRemove) {
							map.removeAll(attribute);
						}
						livingEntity.getAttributes().addTransientAttributeModifiers(map);
						handler.addTransientSlotModifiers(slots);
						currentCurio.ifPresent(curio -> curio.onEquip(slotContext, prevStack));
						if (livingEntity instanceof ServerPlayer) {
							EquipCurioTrigger.INSTANCE.trigger((ServerPlayer) livingEntity, stack,
									(ServerLevel) livingEntity.level(), livingEntity.getX(),
									livingEntity.getY(), livingEntity.getZ());
						}
					}
					stackHandler.setPreviousStackInSlot(i, stack.copy());
				}
				Set<ICurioStacksHandler> updates = handler.getUpdatingInventories();
				if (!updates.isEmpty()) {
					NetworkHandler.INSTANCE.send(
							PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity),
							new SPacketSyncModifiers(livingEntity.getId(), updates));
					updates.clear();
				}
			}
		}
	}

	private static void syncCurios(LivingEntity livingEntity, ItemStack stack,
								   LazyOptional<ICurio> currentCurio, LazyOptional<ICurio> prevCurio,
								   String identifier, int index, boolean cosmetic, boolean visible,
								   SPacketSyncStack.HandlerType type) {
		SlotContext slotContext = new SlotContext(identifier, livingEntity, index, cosmetic, visible);
		boolean syncable = currentCurio.map(curio -> curio.canSync(slotContext)).orElse(false) ||
				prevCurio.map(curio -> curio.canSync(slotContext)).orElse(false);
		CompoundTag syncTag = syncable ? currentCurio.map(curio -> {
			CompoundTag tag = curio.writeSyncData(slotContext);
			return tag != null ? tag : new CompoundTag();
		}).orElse(new CompoundTag()) : new CompoundTag();
		NetworkHandler.INSTANCE
				.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity),
						new SPacketSyncStack(livingEntity.getId(), identifier, index, stack, type,
								syncTag));
	}

}
