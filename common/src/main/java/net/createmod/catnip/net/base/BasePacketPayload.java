package net.createmod.catnip.net.base;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public sealed interface BasePacketPayload extends CustomPacketPayload permits ClientboundPacketPayload, ServerboundPacketPayload {
	PacketTypeProvider getTypeProvider();

	@Override
	@ApiStatus.NonExtendable
	default Type<? extends CustomPacketPayload> type() {
		return this.getTypeProvider().getType();
	}

	interface PacketTypeProvider {
		<T extends CustomPacketPayload> Type<T> getType();
	}
}
