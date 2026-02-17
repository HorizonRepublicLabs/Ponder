package net.createmod.catnip.impl.fabric.client;

import net.createmod.catnip.api.client.animation.AnimationTickHolder;
import net.createmod.catnip.impl.client.CatnipReloadListener;
import net.createmod.catnip.impl.client.placement.PlacementClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.InvalidateRenderStateCallback;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;

public final class CatnipFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(CatnipReloadListener.ID, CatnipReloadListener.INSTANCE);

		InvalidateRenderStateCallback.EVENT.register(AnimationTickHolder::reset);
		// TODO
		//WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> PonderClient.onRenderWorld(context.matrixStack()));

		HudRenderCallback.EVENT.register((graphics, _) -> PlacementClient.onRenderCrosshairOverlay(graphics, AnimationTickHolder.getPartialTicksUI()));
	}
}
