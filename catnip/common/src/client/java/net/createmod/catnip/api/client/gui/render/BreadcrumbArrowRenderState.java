package net.createmod.catnip.api.client.gui.render;

import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.createmod.catnip.api.client.render.PonderRenderPipelines;
import net.createmod.catnip.api.theme.Color;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;

public record BreadcrumbArrowRenderState(
	Matrix3x2f pose, int width, int height, int indent, Color startColor, Color endColor
) implements GuiElementRenderState {
	@Override
	public RenderPipeline pipeline() {
		return PonderRenderPipelines.POSITION_COLOR_TRIANGLES;
	}

	@Override
	public void buildVertices(VertexConsumer consumer) {
		/*
		 * 0,0       x1,y0 ********************* x2,y0 ***** x3,y0
		 *       ****                                     ****
		 *   ****                                     ****
		 * x0,y1     x1,y1                       x2,y1
		 *   ****                                     ****
		 *       ****                                     ****
		 *           x1,y2 ********************* x2,y2 ***** x3,y2
		 *
		 */

		float x0 = 0;
		float x1 = indent;
		float x2 = width;
		float x3 = indent + width;

		float y0 = 0;
		float y1 = height / 2f;
		float y2 = height;

		int indentAbs = Math.abs(indent);
		int widthAbs = Math.abs(width);
		Color c1 = Color.mixColors(startColor, endColor, 0);
		Color c2 = Color.mixColors(startColor, endColor, (indentAbs) / (widthAbs + 2f * indentAbs));
		Color c3 = Color.mixColors(startColor, endColor, (indentAbs + widthAbs) / (widthAbs + 2f * indentAbs));
		Color c4 = Color.mixColors(startColor, endColor, 1);

		consumer.addVertexWith2DPose(pose, x0, y1).setColor(c1.getRGB());
		consumer.addVertexWith2DPose(pose, x1, y0).setColor(c2.getRGB());
		consumer.addVertexWith2DPose(pose, x1, y1).setColor(c2.getRGB());

		consumer.addVertexWith2DPose(pose, x0, y1).setColor(c1.getRGB());
		consumer.addVertexWith2DPose(pose, x1, y1).setColor(c2.getRGB());
		consumer.addVertexWith2DPose(pose, x1, y2).setColor(c2.getRGB());

		consumer.addVertexWith2DPose(pose, x1, y2).setColor(c2.getRGB());
		consumer.addVertexWith2DPose(pose, x1, y0).setColor(c2.getRGB());
		consumer.addVertexWith2DPose(pose, x2, y0).setColor(c3.getRGB());

		consumer.addVertexWith2DPose(pose, x1, y2).setColor(c2.getRGB());
		consumer.addVertexWith2DPose(pose, x2, y0).setColor(c3.getRGB());
		consumer.addVertexWith2DPose(pose, x2, y2).setColor(c3.getRGB());

		consumer.addVertexWith2DPose(pose, x2, y1).setColor(c3.getRGB());
		consumer.addVertexWith2DPose(pose, x2, y0).setColor(c3.getRGB());
		consumer.addVertexWith2DPose(pose, x3, y0).setColor(c4.getRGB());

		consumer.addVertexWith2DPose(pose, x2, y2).setColor(c3.getRGB());
		consumer.addVertexWith2DPose(pose, x2, y1).setColor(c3.getRGB());
		consumer.addVertexWith2DPose(pose, x3, y2).setColor(c4.getRGB());
	}

	@Override
	public TextureSetup textureSetup() {
		return TextureSetup.noTexture();
	}

	@Override
	public @Nullable ScreenRectangle scissorArea() {
		return null;
	}

	@Override
	public ScreenRectangle bounds() {
		return new ScreenRectangle(0, 0, width, height).transformMaxBounds(pose);
	}
}
