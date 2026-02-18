package net.createmod.ponder.fabric;

import net.createmod.ponder.impl.client.PonderClient;
import net.createmod.ponder.impl.client.PonderKeybinds;
import net.createmod.ponder.impl.client.PonderTooltipHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.Minecraft;

public class FabricPonderClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		PonderClient.init();

		ClientTickEvents.END_CLIENT_TICK.register(_ -> PonderTooltipHandler.tick());

		ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> PonderTooltipHandler.addToTooltip(lines, stack));
		PonderKeybinds.register(KeyMappingHelper::registerKeyMapping);

		ClientLifecycleEvents.CLIENT_STARTED.register(FabricPonderClient::onClientStarted);

		prepareConfigUI();
	}

	private void prepareConfigUI() {
		// FIXME: config
		// BaseConfigScreen.setDefaultActionFor(Ponder.MOD_ID, base -> base
		// 		.withButtonLabels("Client Settings", null, null)
		// 		.withSpecs(PonderConfig.client().specification, null, null)
		// );
	}

	private static void onClientStarted(Minecraft client) {
		PonderClient.modLoadCompleted();
	}
}
