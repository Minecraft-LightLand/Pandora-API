
package dev.xkmc.pandora.content.menu;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.pandora.init.Pandora;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class OpenPandoraPacket extends SerialPacketBase {
	@SerialField
	public OpenPandoraHandler event;

	/**
	 * @deprecated
	 */
	@Deprecated
	public OpenPandoraPacket() {
	}

	public OpenPandoraPacket(OpenPandoraHandler event) {
		this.event = event;
	}

	public void handle(NetworkEvent.Context context) {
		if (context.getSender() != null) {
			new PandoraMenuPvd(Pandora.LIST_MENU.get(), 0).open(context.getSender());
		}

	}
}
