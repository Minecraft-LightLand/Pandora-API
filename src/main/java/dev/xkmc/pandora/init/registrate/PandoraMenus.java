package dev.xkmc.pandora.init.registrate;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.pandora.content.menu.edit.PandoraEditMenu;
import dev.xkmc.pandora.content.menu.edit.PandoraEditScreen;
import dev.xkmc.pandora.init.Pandora;
import net.minecraft.resources.ResourceLocation;

public class PandoraMenus {

	private static final ResourceLocation DUMMY = L2Tabs.loc(Pandora.MODID);

	//public static final MenuEntry<PandoraListMenu> LIST_MENU = Pandora.REGISTRATE.menu("pandora_list", PandoraListMenu::fromNetwork, () -> PandoraListScreen::new).register();

	public static final MenuEntry<PandoraEditMenu> EDIT_MENU = Pandora.REGISTRATE.menu("pandora_edit",
			PandoraEditMenu::fromNetwork, () -> PandoraEditScreen::new).register();

	//public static Val<TabToken<InvTabData, TabPandora>> TAB_PANDORA = SR.of(Pandora.REG, L2Tabs.TABS.reg()).reg("pandora", () -> L2Tabs.GROUP.registerTab(() -> TabPandora::new, PandoraLangData.TITLE.get()));

	public static void register() {

	}

	public static void genDataMap(RegistrateDataMapProvider pvd) {
		//pvd.builder(L2Tabs.ORDER.reg()).add(TAB_PANDORA.id(), 2010, false);
		//pvd.builder(L2Tabs.ICON.reg()).add(TAB_PANDORA.id(), PandoraItems.PANDORA_NECKLACE.get(), false);
	}
}
