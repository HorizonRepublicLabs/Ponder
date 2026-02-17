package net.createmod.catnip.api.client.platform;

import net.createmod.catnip.impl.ServiceHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface ClientNetworkHelper {
	ClientNetworkHelper INSTANCE = ServiceHelper.load(ClientNetworkHelper.class);

	void sendToServer(CustomPacketPayload payload);
}
