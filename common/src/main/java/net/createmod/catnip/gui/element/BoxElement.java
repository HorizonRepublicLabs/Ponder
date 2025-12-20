package net.createmod.catnip.gui.element;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;

import net.minecraft.client.renderer.RenderPipelines;

import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

import net.createmod.catnip.data.Couple;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.gui.GuiGraphics;

import org.jspecify.annotations.Nullable;

public class BoxElement extends AbstractRenderElement {

	public static final Couple<Color> COLOR_VANILLA_BORDER = Couple.create(
		new Color(0x50_5000ff, true),
		new Color(0x50_28007f, true)
	).map(Color::setImmutable);
	public static final Color COLOR_VANILLA_BACKGROUND = new Color(0xf0_100010, true).setImmutable();
	public static final Color COLOR_BACKGROUND_FLAT = new Color(0xff_000000, true).setImmutable();
	public static final Color COLOR_BACKGROUND_TRANSPARENT = new Color(0xdd_000000, true).setImmutable();

	protected Color background = COLOR_VANILLA_BACKGROUND;
	protected Color borderTop = COLOR_VANILLA_BORDER.getFirst();
	protected Color borderBot = COLOR_VANILLA_BORDER.getSecond();
	protected int borderOffset = 2;

	public <T extends BoxElement> T withBackground(Color color) {
		this.background = color;
		//noinspection unchecked
		return (T) this;
	}

	public <T extends BoxElement> T withBackground(int color) {
		return withBackground(new Color(color, true));
	}

	public <T extends BoxElement> T flatBorder(Color color) {
		this.borderTop = color;
		this.borderBot = color;
		//noinspection unchecked
		return (T) this;
	}

	public <T extends BoxElement> T flatBorder(int color) {
		return flatBorder(new Color(color, true));
	}

	public <T extends BoxElement> T gradientBorder(Couple<Color> colors) {
		this.borderTop = colors.getFirst();
		this.borderBot = colors.getSecond();
		//noinspection unchecked
		return (T) this;
	}

	public <T extends BoxElement> T gradientBorder(Color top, Color bot) {
		this.borderTop = top;
		this.borderBot = bot;
		//noinspection unchecked
		return (T) this;
	}

	public <T extends BoxElement> T gradientBorder(int top, int bot) {
		return gradientBorder(new Color(top, true), new Color(bot, true));
	}

	public <T extends BoxElement> T withBorderOffset(int offset) {
		this.borderOffset = offset;
		//noinspection unchecked
		return (T) this;
	}

	@Override
	public void render(GuiGraphics graphics) {
		submitBox(graphics);
	}

	protected void submitBox(GuiGraphics graphics) {
		Color c1 = background.copy().scaleAlpha(alpha);
		Color c2 = borderTop.copy().scaleAlpha(alpha);
		Color c3 = borderBot.copy().scaleAlpha(alpha);
		graphics.guiRenderState.submitGuiElement(new BoxRenderState(RenderPipelines.GUI, new Matrix3x2f(graphics.pose()), width, height, x, y, borderOffset, c1, c2, c3, new ScreenRectangle((int) x, (int) y, width, height), null));
	}

	//total box width = 1 * 2 (outer border) + 1 * 2 (inner color border) + 2 * borderOffset + width
	//defaults to 2 + 2 + 4 + 16 = 24px
	public record BoxRenderState(RenderPipeline pipeline, Matrix3x2fc pose, int width, int height, float x, float y, int borderOffset, Color c1, Color c2, Color c3, @Nullable ScreenRectangle bounds, @Nullable ScreenRectangle scissorArea) implements GuiElementRenderState {

		@Override
		public void buildVertices(VertexConsumer b) {
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
			int f = borderOffset();
			//outer top
			b.addVertexWith2DPose(pose, x - f - 1, y - f - 2).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x - f - 1, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y - f - 2).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			//outer left
			b.addVertexWith2DPose(pose, x - f - 2, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x - f - 2, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x - f - 1, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x - f - 1, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			//outer bottom
			b.addVertexWith2DPose(pose, x - f - 1, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x - f - 1, y + f + 2 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y + f + 2 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			//outer right
			b.addVertexWith2DPose(pose, x + f + 1 + width, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 2 + width, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 2 + width, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			//inner background - also render behind the inner edges
			b.addVertexWith2DPose(pose, x - f - 1, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x - f - 1, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y + f + 1 + height).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y - f - 1).setColor(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
			//inner top - includes corners
			b.addVertexWith2DPose(pose, x - f - 1, y - f - 1).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
			b.addVertexWith2DPose(pose, x - f - 1, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y - f - 1).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
			//inner left - excludes corners
			b.addVertexWith2DPose(pose, x - f - 1, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
			b.addVertexWith2DPose(pose, x - f - 1, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
			b.addVertexWith2DPose(pose, x - f, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
			b.addVertexWith2DPose(pose, x - f, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
			//inner bottom - includes corners
			b.addVertexWith2DPose(pose, x - f - 1, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
			b.addVertexWith2DPose(pose, x - f - 1, y + f + 1 + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y + f + 1 + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
			//inner right - excludes corners
			b.addVertexWith2DPose(pose, x + f + width, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
			b.addVertexWith2DPose(pose, x + f + width, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y + f + height).setColor(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha());
			b.addVertexWith2DPose(pose, x + f + 1 + width, y - f).setColor(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
		}

		@Override
		public TextureSetup textureSetup() {
			return TextureSetup.noTexture();
		}
	}
}
