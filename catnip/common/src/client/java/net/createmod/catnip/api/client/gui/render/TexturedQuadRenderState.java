package net.createmod.catnip.api.client.gui.render;

import org.joml.Matrix3x2f;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.createmod.catnip.api.theme.Color;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.renderer.RenderPipelines;

public record TexturedQuadRenderState(
	Matrix3x2f pose, ScreenRectangle scissorArea, TextureSetup textureSetup, Color color,
	int left, int right, int top, int bot, float u1, float u2, float v1, float v2
) implements GuiElementRenderState {
	@Override
	public RenderPipeline pipeline() {
		return RenderPipelines.GUI_TEXTURED;
	}

	@Override
	public void buildVertices(VertexConsumer consumer) {
		consumer.addVertexWith2DPose(pose, (float) left, (float) bot).setColor(color.getRGB()).setUv(u1, v2);
		consumer.addVertexWith2DPose(pose, (float) right, (float) bot).setColor(color.getRGB()).setUv(u2, v2);
		consumer.addVertexWith2DPose(pose, (float) right, (float) top).setColor(color.getRGB()).setUv(u2, v1);
		consumer.addVertexWith2DPose(pose, (float) left, (float) top).setColor(color.getRGB()).setUv(u1, v1);
	}

	@Override
	public ScreenRectangle bounds() {
		return new ScreenRectangle(left, top, right - left, bot - top).transformMaxBounds(pose);
	}
}
