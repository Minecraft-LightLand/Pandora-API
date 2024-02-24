package dev.xkmc.pandora.content.core;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.EnumUtils;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.SlotModifiersUpdatedEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;

import java.util.*;

public class PandoraCurioStacksHandler implements ICurioStacksHandler {

	private static final UUID LEGACY_UUID = UUID.fromString("0b0eabbd-4220-4e9f-bafb-34100da2bd7e");

	private final ICuriosItemHandler itemHandler;
	private final String identifier;
	private final Map<UUID, AttributeModifier> modifiers = new HashMap<>();
	private final Set<AttributeModifier> persistentModifiers = new HashSet<>();
	private final Set<AttributeModifier> cachedModifiers = new HashSet<>();
	private final Multimap<AttributeModifier.Operation, AttributeModifier> modifiersByOperation =
			HashMultimap.create();

	private IDynamicStackHandler stackHandler, cosmeticHandler;
	private ICurio.DropRule dropRule;
	private boolean update;
	private NonNullList<Boolean> renderHandler;

	public PandoraCurioStacksHandler(ICuriosItemHandler itemHandler, String identifier) {
		this(itemHandler, identifier, ICurio.DropRule.DEFAULT);
	}

	public PandoraCurioStacksHandler(ICuriosItemHandler itemHandler, String identifier, ICurio.DropRule dropRule) {

		this.itemHandler = itemHandler;
		this.identifier = identifier;
		this.dropRule = dropRule;
		this.renderHandler = NonNullList.withSize(0, false);
		this.stackHandler = new PandoraDynamicStackHandler(itemHandler, 0, false,
				(index) -> new SlotContext(identifier, itemHandler.getWearer(), index, false, false));
		this.cosmeticHandler = new EmptyDynamicStackHandler(0);
	}

	@Override
	public IDynamicStackHandler getStacks() {
		this.update();
		return this.stackHandler;
	}

	@Override
	public IDynamicStackHandler getCosmeticStacks() {
		this.update();
		return cosmeticHandler;
	}

	@Override
	public NonNullList<Boolean> getRenders() {
		this.update();
		return this.renderHandler;
	}

	@Override
	public boolean canToggleRendering() {
		return false;
	}

	@Override
	public ICurio.DropRule getDropRule() {
		return this.dropRule;
	}

	@Override
	public int getSlots() {
		this.update();
		return this.stackHandler.getSlots();
	}

	@Override
	public int getSizeShift() {
		return 0;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public boolean hasCosmetic() {
		return false;
	}

	@Override
	public void grow(int amount) {
		amount = Math.max(0, amount);

		if (amount > 0) {
			this.addLegacyChange(amount);
		}
	}

	@Override
	public void shrink(int amount) {
		amount = Math.max(0, amount);

		if (amount > 0) {
			this.addLegacyChange(Math.min(this.getSlots(), amount) * -1);
		}
	}

	private void addLegacyChange(int shift) {
		AttributeModifier mod = this.getModifiers().get(LEGACY_UUID);
		int current = mod != null ? (int) mod.getAmount() : 0;
		current += shift;
		AttributeModifier newModifier =
				new AttributeModifier(LEGACY_UUID, "legacy", current, AttributeModifier.Operation.ADDITION);
		this.modifiers.put(newModifier.getId(), newModifier);
		Collection<AttributeModifier> modifiers =
				this.getModifiersByOperation(newModifier.getOperation());
		modifiers.remove(newModifier);
		modifiers.add(newModifier);
		this.persistentModifiers.remove(newModifier);
		this.persistentModifiers.add(newModifier);
		this.flagUpdate();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag compoundNBT = new CompoundTag();
		compoundNBT.put("Stacks", this.stackHandler.serializeNBT());
		compoundNBT.putString("DropRule", this.dropRule.toString());

		if (!this.persistentModifiers.isEmpty()) {
			ListTag list = new ListTag();

			for (AttributeModifier attributeModifier : this.persistentModifiers) {
				list.add(attributeModifier.save());
			}
			compoundNBT.put("PersistentModifiers", list);
		}

		if (!this.modifiers.isEmpty()) {
			ListTag list = new ListTag();
			this.modifiers.forEach((uuid, modifier) -> {
				if (!this.persistentModifiers.contains(modifier)) {
					list.add(modifier.save());
				}
			});
			compoundNBT.put("CachedModifiers", list);
		}
		return compoundNBT;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		if (nbt.contains("Stacks")) {
			this.stackHandler.deserializeNBT(nbt.getCompound("Stacks"));
		}
		if (nbt.contains("DropRule")) {
			this.dropRule =
					EnumUtils.getEnum(ICurio.DropRule.class, nbt.getString("DropRule"), this.dropRule);
		}

		if (nbt.contains("PersistentModifiers", 9)) {
			ListTag list = nbt.getList("PersistentModifiers", 10);

			for (int i = 0; i < list.size(); ++i) {
				AttributeModifier attributeModifier = AttributeModifier.load(list.getCompound(i));

				if (attributeModifier != null) {
					this.addPermanentModifier(attributeModifier);
				}
			}
		}

		if (nbt.contains("CachedModifiers", 9)) {
			ListTag list = nbt.getList("CachedModifiers", 10);

			for (int i = 0; i < list.size(); ++i) {
				AttributeModifier attributeModifier = AttributeModifier.load(list.getCompound(i));

				if (attributeModifier != null) {
					this.cachedModifiers.add(attributeModifier);
					this.addTransientModifier(attributeModifier);
				}
			}
		}
		this.update();
	}

	@Override
	public String getIdentifier() {
		return this.identifier;
	}

	public CompoundTag getSyncTag() {
		CompoundTag compoundNBT = new CompoundTag();
		compoundNBT.putString("DropRule", this.dropRule.toString());

		if (!this.modifiers.isEmpty()) {
			ListTag list = new ListTag();

			for (Map.Entry<UUID, AttributeModifier> modifier : this.modifiers.entrySet()) {
				list.add(modifier.getValue().save());
			}
			compoundNBT.put("Modifiers", list);
		}
		return compoundNBT;
	}

	public void applySyncTag(CompoundTag tag) {
		if (tag.contains("DropRule")) {
			this.dropRule =
					EnumUtils.getEnum(ICurio.DropRule.class, tag.getString("DropRule"), this.dropRule);
		}
		this.modifiers.clear();
		this.persistentModifiers.clear();
		this.modifiersByOperation.clear();

		if (tag.contains("Modifiers", 9)) {
			ListTag list = tag.getList("Modifiers", 10);

			for (int i = 0; i < list.size(); ++i) {
				AttributeModifier attributeModifier = AttributeModifier.load(list.getCompound(i));

				if (attributeModifier != null) {
					this.addTransientModifier(attributeModifier);
				}
			}
		}
		this.flagUpdate();
		this.update();
	}

	@Override
	public void copyModifiers(ICurioStacksHandler other) {
		this.modifiers.clear();
		this.cachedModifiers.clear();
		this.modifiersByOperation.clear();
		this.persistentModifiers.clear();
		other.getModifiers().forEach((uuid, modifier) -> this.addTransientModifier(modifier));
		this.cachedModifiers.addAll(other.getCachedModifiers());

		for (AttributeModifier persistentModifier : other.getPermanentModifiers()) {
			this.addPermanentModifier(persistentModifier);
		}
		this.update();
	}

	public Map<UUID, AttributeModifier> getModifiers() {
		return this.modifiers;
	}

	@Override
	public Set<AttributeModifier> getPermanentModifiers() {
		return this.persistentModifiers;
	}

	@Override
	public Set<AttributeModifier> getCachedModifiers() {
		return this.cachedModifiers;
	}

	public Collection<AttributeModifier> getModifiersByOperation(
			AttributeModifier.Operation operation) {
		return this.modifiersByOperation.get(operation);
	}

	public void addTransientModifier(AttributeModifier modifier) {
		this.modifiers.put(modifier.getId(), modifier);
		this.getModifiersByOperation(modifier.getOperation()).add(modifier);
		this.flagUpdate();
	}

	public void addPermanentModifier(AttributeModifier modifier) {
		this.addTransientModifier(modifier);
		this.persistentModifiers.add(modifier);
	}

	public void removeModifier(UUID uuid) {
		AttributeModifier modifier = this.modifiers.remove(uuid);

		if (modifier != null) {
			this.persistentModifiers.remove(modifier);
			this.getModifiersByOperation(modifier.getOperation()).remove(modifier);
			this.flagUpdate();
		}
	}

	private void flagUpdate() {
		this.update = true;

		if (this.itemHandler != null) {
			this.itemHandler.getUpdatingInventories().remove(this);
			this.itemHandler.getUpdatingInventories().add(this);
		}
	}

	public void clearModifiers() {
		Set<UUID> ids = new HashSet<>(this.modifiers.keySet());

		for (UUID id : ids) {
			this.removeModifier(id);
		}
	}

	public void clearCachedModifiers() {

		for (AttributeModifier cachedModifier : this.cachedModifiers) {
			this.removeModifier(cachedModifier.getId());
		}
		this.cachedModifiers.clear();
		this.flagUpdate();
	}

	public void update() {
		if (this.update) {
			this.update = false;
			double baseSize = 0;
			for (AttributeModifier mod : this.getModifiersByOperation(
					AttributeModifier.Operation.ADDITION)) {
				baseSize += mod.getAmount();
			}
			double size = baseSize;
			for (AttributeModifier mod : this.getModifiersByOperation(
					AttributeModifier.Operation.MULTIPLY_BASE)) {
				size += baseSize * mod.getAmount();
			}
			for (AttributeModifier mod : this.getModifiersByOperation(
					AttributeModifier.Operation.MULTIPLY_TOTAL)) {
				size *= mod.getAmount();
			}
			if (size != this.getSlots()) {
				this.resize((int) size);
				if (this.itemHandler != null && this.itemHandler.getWearer() != null) {
					MinecraftForge.EVENT_BUS.post(
							new SlotModifiersUpdatedEvent(this.itemHandler.getWearer(), Set.of(this.identifier)));
					if (this.itemHandler.getWearer() instanceof Player player &&
							player.containerMenu instanceof CuriosContainer) {
						((CuriosContainer) player.containerMenu).resetSlots();
					}
				}
			}
		}
	}

	private void resize(int newSize) {
		int currentSize = this.getSlots();

		if (currentSize != newSize) {
			int change = newSize - currentSize;

			if (currentSize > newSize) {
				change = change * -1;
				this.loseStacks(this.stackHandler, identifier, change);
				this.stackHandler.shrink(change);
			} else {
				this.stackHandler.grow(change);
			}
			renderHandler = NonNullList.withSize(newSize, false);
			this.cosmeticHandler = new EmptyDynamicStackHandler(newSize);
		}
	}

	private void loseStacks(IDynamicStackHandler stackHandler, String identifier, int amount) {

		if (this.itemHandler == null) {
			return;
		}

		PandoraUpdateHandler.update(itemHandler.getWearer(), itemHandler, this, identifier);

		List<ItemStack> drops = new ArrayList<>();

		for (int i = Math.max(0, stackHandler.getSlots() - amount);
			 i >= 0 && i < stackHandler.getSlots(); i++) {
			ItemStack stack = stackHandler.getStackInSlot(i);
			drops.add(stackHandler.getStackInSlot(i));
			LivingEntity entity = this.itemHandler.getWearer();
			SlotContext slotContext = new SlotContext(identifier, entity, i, false, false);

			if (!stack.isEmpty()) {
				UUID uuid = UUID.nameUUIDFromBytes((identifier + i).getBytes());
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
				this.itemHandler.getWearer().getAttributes().removeAttributeModifiers(map);
				this.itemHandler.removeSlotModifiers(slots);
				CuriosApi.getCurio(stack).ifPresent(curio -> curio.onUnequip(slotContext, ItemStack.EMPTY));
			}
			stackHandler.setStackInSlot(i, ItemStack.EMPTY);
		}
		//drops.forEach(this.itemHandler::loseInvalidStack);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PandoraCurioStacksHandler that = (PandoraCurioStacksHandler) o;
		return identifier.equals(that.identifier);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier);
	}

	public ICurioStacksHandler from(ICurioStacksHandler e) {
		copyModifiers(e);
		return this;
	}

}
