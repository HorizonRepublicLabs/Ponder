package net.createmod.ponder.fabric;

import org.jspecify.annotations.Nullable;

import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.placement.PlacementClient;
import net.createmod.catnip.theme.Color;
import net.createmod.ponder.PonderClient;
import net.createmod.ponder.enums.PonderKeybinds;
import net.createmod.ponder.fabric.utility.FabricClientResourceReloadListener;
import net.createmod.ponder.foundation.PonderTooltipHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.InvalidateRenderStateCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;

public class FabricPonderClient implements ClientModInitializer {
	public static final FabricClientResourceReloadListener FABRIC$RESOURCE_RELOAD_LISTENER = new FabricClientResourceReloadListener();

	@Nullable
	public static Couple<Color> tooltipBorderColorOverride;

	@Override
	public void onInitializeClient() {
		PonderClient.init();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			PonderClient.onTick();
			PonderTooltipHandler.tick();
		});

		InvalidateRenderStateCallback.EVENT.register(() -> {
			AnimationTickHolder.reset();
		});
		// TODO
		//WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> PonderClient.onRenderWorld(context.matrixStack()));

		HudRenderCallback.EVENT.register((graphics, deltaTracker) -> PlacementClient.onRenderCrosshairOverlay(graphics, AnimationTickHolder.getPartialTicksUI()));

		ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> PonderTooltipHandler.addToTooltip(lines, stack));
		PonderKeybinds.register(KeyMappingHelper::registerKeyMapping);

		ClientLifecycleEvents.CLIENT_STARTED.register(FabricPonderClient::onClientStarted);

		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(FABRIC$RESOURCE_RELOAD_LISTENER);

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
