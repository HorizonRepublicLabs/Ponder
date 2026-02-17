package net.createmod.catnip.api.network.packets;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import io.netty.buffer.ByteBuf;
import net.createmod.catnip.api.network.CatnipPackets;
import net.createmod.catnip.api.network.SimpleCatnipActions;
import net.createmod.catnip.api.network.base.ClientboundPacketPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClientboundSimpleActionPacket(String action, String value) implements ClientboundPacketPayload {
	public static final StreamCodec<ByteBuf, ClientboundSimpleActionPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8, ClientboundSimpleActionPacket::action,
		ByteBufCodecs.STRING_UTF8, ClientboundSimpleActionPacket::value,
		ClientboundSimpleActionPacket::new
	);

	private static final Logger logger = LogUtils.getLogger();
	private static final Map<String, Supplier<Consumer<String>>> actions = new HashMap<>();

	public static void addAction(String name, Supplier<Consumer<String>> action) {
		actions.put(name, action);
	}

	static {
		addAction("test", () -> System.out::println);
		addAction("configScreen", () -> SimpleCatnipActions::configScreen);
	}

	@Override
	public PacketTypeProvider getTypeProvider() {
		return CatnipPackets.CLIENTBOUND_SIMPLE_ACTION;
	}

	@Override
	public void handle(LocalPlayer player) {
		if (!actions.containsKey(action)) {
			logger.warn("Received ClientboundSimpleActionPacket with invalid Action {}, ignoring the packet", action);
			return;
		}

		Minecraft.getInstance().execute(() -> actions.get(action).get().accept(value));
	}
}
