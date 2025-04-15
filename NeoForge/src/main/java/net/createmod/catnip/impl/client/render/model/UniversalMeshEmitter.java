package net.createmod.catnip.impl.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack.Pose;

import org.jetbrains.annotations.UnknownNullability;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.createmod.catnip.client.render.model.ShadeSeparatedBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;

// Modified from https://github.com/Engine-Room/Flywheel/blob/2f67f54c8898d91a48126c3c753eefa6cd224f84/forge/src/lib/java/dev/engine_room/flywheel/lib/model/baked/MeshEmitter.java
class UniversalMeshEmitter implements VertexConsumer {
	@UnknownNullability
	private ShadeSeparatedBufferSource bufferSource;
	@UnknownNullability
	private RenderType layer;

	public void prepare(ShadeSeparatedBufferSource bufferSource, RenderType layer) {
		this.bufferSource = bufferSource;
		this.layer = layer;
	}

	public void clear() {
		bufferSource = null;
	}

	@Override
	public void putBulkData(Pose pose, BakedQuad quad, float red, float green, float blue, float alpha, int light, int overlay) {
		VertexConsumer buffer = bufferSource.getBuffer(layer, quad.isShade());
		buffer.putBulkData(pose, quad, red, green, blue, alpha, light, overlay);
	}

	@Override
	public void putBulkData(PoseStack.Pose pose, BakedQuad quad, float red, float green, float blue, float alpha, int light, int overlay, boolean readExistingColor) {
		VertexConsumer buffer = bufferSource.getBuffer(layer, quad.isShade());
		buffer.putBulkData(pose, quad, red, green, blue, alpha, light, overlay, readExistingColor);
	}

	@Override
	public void putBulkData(PoseStack.Pose pose, BakedQuad quad, float[] brightnesses, float red, float green, float blue, float alpha, int[] lights, int overlay, boolean readExistingColor) {
		VertexConsumer buffer = bufferSource.getBuffer(layer, quad.isShade());
		buffer.putBulkData(pose, quad, brightnesses, red, green, blue, alpha, lights, overlay, readExistingColor);
	}

	@Override
	public VertexConsumer addVertex(float x, float y, float z) {
		throw new UnsupportedOperationException("UniversalMeshEmitter only supports putBulkData!");
	}

	@Override
	public VertexConsumer setColor(int red, int green, int blue, int alpha) {
		throw new UnsupportedOperationException("UniversalMeshEmitter only supports putBulkData!");
	}

	@Override
	public VertexConsumer setUv(float u, float v) {
		throw new UnsupportedOperationException("UniversalMeshEmitter only supports putBulkData!");
	}

	@Override
	public VertexConsumer setUv1(int u, int v) {
		throw new UnsupportedOperationException("UniversalMeshEmitter only supports putBulkData!");
	}

	@Override
	public VertexConsumer setUv2(int u, int v) {
		throw new UnsupportedOperationException("UniversalMeshEmitter only supports putBulkData!");
	}

	@Override
	public VertexConsumer setNormal(float x, float y, float z) {
		throw new UnsupportedOperationException("UniversalMeshEmitter only supports putBulkData!");
	}
}
