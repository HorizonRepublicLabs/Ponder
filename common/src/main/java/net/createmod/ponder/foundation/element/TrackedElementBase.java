package net.createmod.ponder.foundation.element;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.createmod.ponder.api.element.TrackedElement;
import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.state.CameraRenderState;

public abstract class TrackedElementBase<T> extends PonderElementBase implements TrackedElement<T> {
	private final WeakReference<T> reference;

	public TrackedElementBase(T wrapped) {
		this.reference = new WeakReference<>(wrapped);
	}

	@Override
	public void ifPresent(Consumer<T> func) {
		T resolved = reference.get();
		if (resolved == null)
			return;
		func.accept(resolved);
	}

	@Override
	public void renderFirst(PonderLevel world, MultiBufferSource buffer, SubmitNodeCollector queue, Camera camera, CameraRenderState cameraRenderState, PoseStack poseStack, float pt) {
	}

	@Override
	public void renderLayer(PonderLevel world, MultiBufferSource buffer, ChunkSectionLayer layer, SubmitNodeCollector queue, Camera camera, CameraRenderState cameraRenderState, PoseStack poseStack, float pt) {
	}

	@Override
	public void renderLast(PonderLevel world, MultiBufferSource buffer, SubmitNodeCollector queue, Camera camera, CameraRenderState cameraRenderState, PoseStack poseStack, float pt) {
	}
}
