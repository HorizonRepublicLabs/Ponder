package net.createmod.catnip.gui;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.createmod.catnip.gui.render.BreadcrumbArrowRenderState;
import net.createmod.catnip.gui.render.GradientRectRenderState;
import net.createmod.catnip.gui.render.RadialSectorRenderState;
import net.createmod.catnip.gui.render.TexturedQuadRenderState;

import net.minecraft.client.gui.render.TextureSetup;

import org.joml.Matrix3f;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;

import net.createmod.catnip.data.Couple;
import net.createmod.catnip.platform.CatnipClientServices;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;

public class UIRenderHelper {
	public static final Couple<Color> COLOR_TEXT = Couple.create(
		new Color(0xff_eeeeee),
		new Color(0xff_a3a3a3)
	).map(Color::setImmutable);
	public static final Couple<Color> COLOR_TEXT_DARKER = Couple.create(
		new Color(0xff_a3a3a3),
		new Color(0xff_808080)
	).map(Color::setImmutable);
	public static final Couple<Color> COLOR_TEXT_ACCENT = Couple.create(
		new Color(0xff_ddeeff),
		new Color(0xff_a0b0c0)
	).map(Color::setImmutable);
	public static final Couple<Color> COLOR_TEXT_STRONG_ACCENT = Couple.create(
		new Color(0xff_8ab6d6),
		new Color(0xff_6e92ab)
	).map(Color::setImmutable);

	public static final Color COLOR_STREAK = new Color(0x101010, false).setImmutable();

	/**
	 * @param angle   angle in degrees, 0 means fading to the right
	 * @param x       x-position of the starting edge middle point
	 * @param y       y-position of the starting edge middle point
	 * @param breadth total width of the streak
	 * @param length  total length of the streak
	 */
	public static void streak(GuiGraphics graphics, float angle, int x, int y, int breadth, int length) {
		streak(graphics, angle, x, y, breadth, length, COLOR_STREAK);
	}

	public static void streak(GuiGraphics graphics, float angle, int x, int y, int breadth, int length, Color c) {
		Color color = c.copy().setImmutable();
		Color c1 = color.scaleAlpha(0.625f);
		Color c2 = color.scaleAlpha(0.5f);
		Color c3 = color.scaleAlpha(0.0625f);
		Color c4 = color.scaleAlpha(0f);

		Matrix3x2fStack poseStack = graphics.pose();
		poseStack.pushMatrix();
		poseStack.translate(x, y);
		poseStack.rotate((float) ((angle - 90) * (Math.PI / 180)));

		streak(graphics, breadth / 2, length, c1, c2, c3, c4);

		poseStack.popMatrix();
	}

	private static void streak(GuiGraphics graphics, int width, int height, Color c1, Color c2, Color c3, Color c4) {
		if (NavigatableSimiScreen.isCurrentlyRenderingPreviousScreen())
			return;

		double split1 = .5;
		double split2 = .75;
		graphics.fillGradient(-width, 0, width, (int) (split1 * height), c1.getRGB(), c2.getRGB());
		graphics.fillGradient(-width, (int) (split1 * height), width, (int) (split2 * height), c2.getRGB(), c3.getRGB());
		graphics.fillGradient(-width, (int) (split2 * height), width, height, c3.getRGB(), c4.getRGB());
	}

	/**
	 * @see #angledGradient(GuiGraphics, float, int, int, float, float, Color, Color)
	 */
	public static void angledGradient(GuiGraphics graphics, float angle, int x, int y, float breadth, float length, Couple<Color> c) {
		angledGradient(graphics, angle, x, y, breadth, length, c.getFirst(), c.getSecond());
	}

	/**
	 * x and y specify the middle point of the starting edge
	 *
	 * @param angle      the angle of the gradient in degrees; 0° means from left to right
	 * @param startColor the color at the starting edge
	 * @param endColor   the color at the ending edge
	 * @param breadth    the total width of the gradient
	 */
	public static void angledGradient(GuiGraphics graphics, float angle, int x, int y, float breadth, float length, Color startColor, Color endColor) {
		Matrix3x2fStack poseStack = graphics.pose();
		poseStack.pushMatrix();
		poseStack.translate(x, y);
		poseStack.rotate((float) ((angle - 90) * (Math.PI / 180)));

		float w = breadth / 2;
		//graphics.fillGradient(-w, 0, w, length, startColor.getRGB(), endColor.getRGB());
		drawGradientRect(graphics, -w, 0f, w, length, startColor, endColor);

		poseStack.popMatrix();
	}

	public static void drawGradientRect(GuiGraphics graphics, float left, float top, float right, float bottom, Color startColor, Color endColor) {
		graphics.guiRenderState.submitGuiElement(new GradientRectRenderState(
			new Matrix3x2f(graphics.pose()),
			left,
			top,
			right,
			bottom,
			startColor,
			endColor
		));
	}

	public static void breadcrumbArrow(GuiGraphics graphics, int x, int y, int width, int height, int indent, Couple<Color> colors) {
		breadcrumbArrow(graphics, x, y, width, height, indent, colors.getFirst(), colors.getSecond());
	}

	// draws a wide chevron-style breadcrumb arrow pointing left
	public static void breadcrumbArrow(GuiGraphics graphics, int x, int y, int width, int height, int indent, Color startColor, Color endColor) {
		Matrix3x2fStack poseStack = graphics.pose();
		poseStack.pushMatrix();
		poseStack.translate(x - indent, y);

		graphics.guiRenderState.submitGuiElement(new BreadcrumbArrowRenderState(
			new Matrix3x2f(graphics.pose()),
			width,
			height,
			indent,
			startColor,
			endColor
		));

		poseStack.popMatrix();
	}

	/**
	 * centered on 0, 0
	 *
	 * @param arcAngle length of the sector arc
	 */
	public static void drawRadialSector(GuiGraphics graphics, float innerRadius, float outerRadius, float startAngle, float arcAngle, Color innerColor, Color outerColor) {
		graphics.guiRenderState.submitGuiElement(RadialSectorRenderState.create(
			new Matrix3x2f(graphics.pose()),
			innerRadius,
			outerRadius,
			startAngle,
			arcAngle,
			innerColor,
			outerColor
		));
	}

	//just like AbstractGui#drawTexture, but with a color at every vertex
	public static void drawColoredTexture(GuiGraphics graphics, TextureSetup texture, Color c, int x, int y, int texLeft, int texTop, int width, int height) {
		drawColoredTexture(graphics, texture, c, x, y, (float) texLeft, (float) texTop, width, height, 256, 256);
	}

	public static void drawColoredTexture(GuiGraphics graphics, TextureSetup texture, Color c, int x, int y, float texLeft, float texTop, int width, int height, int sheetWidth, int sheetHeight) {
		//noinspection SuspiciousNameCombination
		drawColoredTexture(graphics, texture, c, x, x + width, y, y + height, width, height, texLeft, texTop, sheetWidth, sheetHeight);
	}

	public static void drawStretched(GuiGraphics graphics, int left, int top, int w, int h, TextureSheetSegment tex) {
		drawTexturedQuad(
			graphics, tex.bind(), Color.WHITE, left, left + w, top, top + h,
			tex.getStartX() / 256f, (tex.getStartX() + tex.getWidth()) / 256f,
			tex.getStartY() / 256f, (tex.getStartY() + tex.getHeight()) / 256f
		);
	}

	public static void drawCropped(GuiGraphics graphics, int left, int top, int w, int h, TextureSheetSegment tex) {
		drawTexturedQuad(
			graphics, tex.bind(), Color.WHITE, left, left + w, top, top + h,
			tex.getStartX() / 256f, (tex.getStartX() + w) / 256f,
			tex.getStartY() / 256f, (tex.getStartY() + h) / 256f
		);
	}

	private static void drawColoredTexture(GuiGraphics graphics, TextureSetup texture, Color c, int left, int right, int top, int bot, int texWidth, int texHeight, float texLeft, float texRight, int sheetWidth, int sheetHeight) {
		drawTexturedQuad(graphics, texture, c, left, right, top, bot, (texLeft + 0.0F) / (float) sheetWidth, (texLeft + (float) texWidth) / (float) sheetWidth, (texRight + 0.0F) / (float) sheetHeight, (texRight + (float) texHeight) / (float) sheetHeight);
	}

	private static void drawTexturedQuad(GuiGraphics graphics, TextureSetup texture, Color c, int left, int right, int top, int bot, float u1, float u2, float v1, float v2) {
		graphics.guiRenderState.submitGuiElement(new TexturedQuadRenderState(
			new Matrix3x2f(graphics.pose()),
			graphics.scissorStack.peek(),
			texture,
			c,
			left,
			right,
			top,
			bot,
			u1,
			u2,
			v1,
			v2
		));
	}

	public static void flipForGuiRender(PoseStack poseStack) {
		poseStack.mulPose(new Matrix4f().scaling(1, -1, 1));
	}
}
