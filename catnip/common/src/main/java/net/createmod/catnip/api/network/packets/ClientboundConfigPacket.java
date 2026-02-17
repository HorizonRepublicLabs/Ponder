package net.createmod.catnip.api.network.packets;

import io.netty.buffer.ByteBuf;
import net.createmod.catnip.api.network.CatnipPackets;
import net.createmod.catnip.api.network.base.ClientboundPacketPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClientboundConfigPacket(String path, String value) implements ClientboundPacketPayload {
	public static final StreamCodec<ByteBuf, ClientboundConfigPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8, ClientboundConfigPacket::path,
		ByteBufCodecs.STRING_UTF8, ClientboundConfigPacket::value,
		ClientboundConfigPacket::new
	);

	@Override
	public PacketTypeProvider getTypeProvider() {
		return CatnipPackets.CLIENTBOUND_CONFIG;
	}

	@Override
	public void handle(LocalPlayer player) {/*
		if (Minecraft.getInstance().player == null) {
			return;
		}

		ConfigHelper.ConfigPath path;

		try {
			path = ConfigHelper.ConfigPath.parse(this.path);
		} catch (IllegalArgumentException e) {
			player.displayClientMessage(Ponder.lang().text(e.getMessage()).component(), false);
			return;
		}

		if (path.getType() != ModConfig.Type.CLIENT) {
			Ponder.LOGGER.warn("Received type-mismatched config packet on client");
			return;
		}

		try {
			ConfigHelper.setConfigValue(path, value);
			player.displayClientMessage(Component.literal("Great Success!"), false);
		} catch (ConfigHelper.InvalidValueException e) {
			player.displayClientMessage(Component.literal("Config could not be set the the specified value!"), false);
		} catch (Exception e) {
			player.displayClientMessage(Component.literal("Something went wrong while trying to set config value. Check the client logs for more information"), false);
			Ponder.LOGGER.warn("Exception during client-side config value set:", e);
		}

	*/}
}
