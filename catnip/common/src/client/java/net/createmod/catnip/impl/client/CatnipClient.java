package net.createmod.catnip.impl.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.createmod.catnip.api.client.animation.AnimationTickHolder;
import net.createmod.catnip.api.client.ghostblock.GhostBlocks;
import net.createmod.catnip.api.client.outliner.Outliner;
import net.createmod.catnip.api.client.render.CachedBuffers;
import net.createmod.catnip.api.client.render.DefaultSuperRenderTypeBuffer;
import net.createmod.catnip.api.client.render.SuperByteBufferCache;
import net.createmod.catnip.api.client.render.SuperRenderTypeBuffer;
import net.createmod.catnip.impl.client.placement.PlacementClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public final class CatnipClient {
	public static void init() {
		SuperByteBufferCache.getInstance().registerCompartment(CachedBuffers.GENERIC_BLOCK);
	    CatnipClientPayloadHandlers.register();
	}

	public static void invalidateRenderers() {
		SuperByteBufferCache.getInstance().invalidate();
	}

	public static void onTick() {
		AnimationTickHolder.tick();

		if (!isGameActive())
			return;

		PlacementClient.tick(); // Should be called before GhostBlocks' tick as it can add new ghosts

		GhostBlocks.getInstance().tickGhosts();
		Outliner.getInstance().tickOutlines();
	}

	public static void onRenderWorld(PoseStack ms) {
		Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().position();
		float partialTicks = AnimationTickHolder.getPartialTicks();

		ms.pushPose();
		SuperRenderTypeBuffer buffer = DefaultSuperRenderTypeBuffer.getInstance();

		GhostBlocks.getInstance().renderAll(ms, buffer, cameraPos);
		Outliner.getInstance().renderOutlines(ms, buffer, cameraPos, partialTicks);

		buffer.draw();
		ms.popPose();
	}

	public static boolean isGameActive() {
		return Minecraft.getInstance().level != null && Minecraft.getInstance().player != null;
	}
}
