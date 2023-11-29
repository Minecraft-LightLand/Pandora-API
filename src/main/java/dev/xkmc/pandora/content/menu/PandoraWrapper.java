package dev.xkmc.pandora.content.menu;

import dev.xkmc.l2tabs.compat.BaseCuriosWrapper;
import dev.xkmc.l2tabs.compat.CuriosSlotWrapper;
import dev.xkmc.pandora.content.core.PandoraDynamicStackHandler;
import dev.xkmc.pandora.init.data.PandoraSlotGen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.Optional;

public class PandoraWrapper extends BaseCuriosWrapper {

	private final ArrayList<CuriosSlotWrapper> list = new ArrayList<>();

	public final ArrayList<TitleLine> titles = new ArrayList<>();

	private int row;

	public PandoraWrapper(LivingEntity player, int page) {
		super(player);
		int max = 6;
		Optional<ICuriosItemHandler> opt = player.getCapability(CuriosCapability.INVENTORY).resolve();
		this.page = page;
		if (opt.isEmpty()) {
			total = 0;
			return;
		}
		var cap = opt.get().getCurios().get(PandoraSlotGen.NAME);
		if (!(cap.getStacks() instanceof PandoraDynamicStackHandler pandora))
			return;
		var invs = pandora.getSplitSlots();
		int pageIndex = 0;
		int rowIndex = 0;
		int slotIndex = 0;
		for (var e : invs) {
			int size = (e.getSlots() + 8) / 9 + 1;
			if (size > 1) {
				if (rowIndex > 0 && rowIndex + size > max) {
					rowIndex = 0;
					pageIndex++;
				}
				if (pageIndex == page) {
					for (int i = 0; i < 9; i++) {
						list.add(null);
						titles.add(new TitleLine(rowIndex, e.getTitle()));
					}
					for (int i = 0; i < e.getSlots(); i++) {
						list.add(new CuriosSlotWrapper(player, cap, slotIndex + i, PandoraSlotGen.NAME));
					}
					while (list.size() % 9 != 0) {
						list.add(null);
					}
					row += size;
				}
				rowIndex += size;
			}
			slotIndex += e.getSlots();
		}
		total = pageIndex + 1;
	}

	@Override
	public int getRows() {
		return row;
	}

	public int getSize() {
		return list.size();
	}

	public CuriosSlotWrapper get(int i) {
		return list.get(i);
	}

	@Nullable
	@Override
	public CuriosSlotWrapper getSlotAtPosition(int i) {
		return i >= 0 && i < this.list.size() ? this.list.get(i) : null;
	}

	public record TitleLine(int row, Component text) {

	}

}
