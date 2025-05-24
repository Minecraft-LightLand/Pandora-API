package dev.xkmc.pandora.init.data;

import dev.xkmc.l2core.util.ConfigInit;
import dev.xkmc.pandora.init.Pandora;
import net.neoforged.neoforge.common.ModConfigSpec;

public class PandoraConfig {

	public static class Client extends ConfigInit {

		Client(Builder builder) {
			markL2();
		}

	}

	public static class Server extends ConfigInit {

		public final ModConfigSpec.BooleanValue showItemTitle;

		Server(Builder builder) {
			markL2();
			showItemTitle = builder.text("Show holder item name in pandora tab")
					.define("showItemTitle", false);
		}

	}

	public static final Client CLIENT;
	public static final Server SERVER;

	static {
		CLIENT = Pandora.REGISTRATE.registerClient(Client::new);
		SERVER = Pandora.REGISTRATE.registerSynced(Server::new);
	}

	public static void init() {
	}

}
