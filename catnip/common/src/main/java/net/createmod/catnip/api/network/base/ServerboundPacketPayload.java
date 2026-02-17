package net.createmod.catnip.api.network.base;

import net.minecraft.server.level.ServerPlayer;

public non-sealed interface ServerboundPacketPayload extends BasePacketPayload {
	/**
	 * Called on the main client thread.
	 */
	void handle(ServerPlayer player);
}
