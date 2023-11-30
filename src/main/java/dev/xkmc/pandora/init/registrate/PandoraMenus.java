package dev.xkmc.pandora.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.pandora.content.menu.edit.PandoraEditMenu;
import dev.xkmc.pandora.content.menu.edit.PandoraEditScreen;
import dev.xkmc.pandora.content.menu.tab.PandoraListMenu;
import dev.xkmc.pandora.content.menu.tab.PandoraListScreen;
import dev.xkmc.pandora.init.Pandora;

public class PandoraMenus {


	public static final MenuEntry<PandoraListMenu> LIST_MENU = Pandora.REGISTRATE.menu("pandora_list",
			PandoraListMenu::fromNetwork, () -> PandoraListScreen::new).register();


	public static final MenuEntry<PandoraEditMenu> EDIT_MENU = Pandora.REGISTRATE.menu("pandora_edit",
			PandoraEditMenu::fromNetwork, () -> PandoraEditScreen::new).register();
	public static void register() {

	}

}
