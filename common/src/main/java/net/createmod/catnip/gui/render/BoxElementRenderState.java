package net.createmod.catnip.gui.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.createmod.catnip.theme.Color;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;

import net.minecraft.client.renderer.RenderPipelines;

import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

public record BoxElementRenderState(Matrix3x2f pose, float x, float y, float width, float height, int f,
									Color c1, Color c2, Color c3) implements GuiElementRenderState {
	//total box width = 1 * 2 (outer border) + 1 * 2 (inner color border) + 2 * borderOffset + width
	//defaults to 2 + 2 + 4 + 16 = 24px
	@Override
	public void buildVertices(VertexConsumer consumer) {
		/*
		 *          _____________
		 *        _|_____________|_
		 *       | | ___________ | |
		 *       | | |  |      | | |
		 *       | | |  |      | | |
		 *       | | |--*   |  | | |
		 *       | | |      h  | | |
		 *       | | |  --w-+  | | |
		 *       | | |         | | |
		 *       | | |_________| | |
		 *       |_|_____________|_|
		 *         |_____________|
		 *
		 * */

		//outer top
		consumer.addVertexWith2DPose(pose, x - f - 1, y - f - 2).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x - f - 1, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y - f - 2).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		//outer left
		consumer.addVertexWith2DPose(pose, x - f - 2, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x - f - 2, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x - f - 1, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x - f - 1, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		//outer bottom
		consumer.addVertexWith2DPose(pose, x - f - 1, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x - f - 1, y + f + 2 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y + f + 2 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		//outer right
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 2 + width, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 2 + width, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		//inner background - also render behind the inner edges
		consumer.addVertexWith2DPose(pose, x - f - 1, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x - f - 1, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
		//inner top - includes corners
		consumer.addVertexWith2DPose(pose, x - f - 1, y - f - 1).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
		consumer.addVertexWith2DPose(pose, x - f - 1, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y - f - 1).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
		//inner left - excludes corners
		consumer.addVertexWith2DPose(pose, x - f - 1, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
		consumer.addVertexWith2DPose(pose, x - f - 1, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
		consumer.addVertexWith2DPose(pose, x - f, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
		consumer.addVertexWith2DPose(pose, x - f, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
		//inner bottom - includes corners
		consumer.addVertexWith2DPose(pose, x - f - 1, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
		consumer.addVertexWith2DPose(pose, x - f - 1, y + f + 1 + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y + f + 1 + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
		//inner right - excludes corners
		consumer.addVertexWith2DPose(pose, x + f + width, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + width, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
		consumer.addVertexWith2DPose(pose, x + f + 1 + width, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
	}

	@Override
	public RenderPipeline pipeline() {
		return RenderPipelines.DEBUG_QUADS;
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
		return new ScreenRectangle((int) x, (int) y, (int) width, (int) height).transformMaxBounds(pose);
	}
}
