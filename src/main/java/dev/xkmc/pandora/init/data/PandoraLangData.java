package dev.xkmc.pandora.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.pandora.init.Pandora;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

public enum PandoraLangData {
	TOOLTIP_HOLDER("tooltip.holder", "Holds up to %s Pandora Charms", 1),
	TOOLTIP_CONTAIN("tooltip.contain", "Contains %s charms", 1),
	TOOLTIP_SHIFT("tooltip.shift", "Press SHIFT to show contents, right click on it to open", 0),
	TOOLTIP_DUPLICATE("tooltip.duplicate", "You may have multiple of this charm in one Pandora", 0),
	INVALID("menu.invalid", "Invalid", 0),
	TITLE("menu.title", "Pandora Charms", 0);

	final String id, def;
	final int count;

	PandoraLangData(String id, String def, int count) {
		this.id = id;
		this.def = def;
		this.count = count;
	}

	public MutableComponent get(Object... objs) {
		if (objs.length != count)
			throw new IllegalArgumentException("for " + name() + ": expect " + count + " parameters, got " + objs.length);
		return translate(Pandora.MODID + "." + id, objs);
	}


	public static void addTranslations(RegistrateLangProvider pvd) {
		for (PandoraLangData id : PandoraLangData.values()) {
			pvd.add(Pandora.MODID + "." + id.id, id.def);
		}
		pvd.add("curios.identifier." + PandoraSlotGen.NAME, "Pandora Charm");
		pvd.add("curios.modifiers." + PandoraSlotGen.NAME, "When on Pandora curio:");
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public static MutableComponent translate(String key, Object... objs) {
		return Component.translatable(key, objs);
	}

}
