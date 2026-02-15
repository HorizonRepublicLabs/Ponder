package net.createmod.catnip.config.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.TriConsumer;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;
import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.opengl.GlStateManager;

import net.createmod.catnip.animation.Force;
import net.createmod.catnip.animation.PhysicalFloat;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.createmod.catnip.gui.element.DelegatedStencilElement;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ConfigScreen extends AbstractSimiScreen {

	public static final Map<String, TriConsumer<Screen, GuiGraphics, Float>> backgrounds = new HashMap<>();
	public static final PhysicalFloat cogSpin = PhysicalFloat.create().withLimit(10f).withDrag(0.3).addForce(new Force.Static(.2f));
	@Nullable
	public static String modID = null;
	@Nullable
	protected final Screen parent;

	public static BlockState shadowState = Blocks.POTTED_CRIMSON_ROOTS.defaultBlockState();
	public static DelegatedStencilElement shadowElement = new DelegatedStencilElement(
		(graphics, x, y, alpha) -> renderCog(graphics),
		(graphics, x, y, alpha) -> graphics.fill(-200, -200, 200, 200, 0x60_000000)
	);

	public ConfigScreen(@Nullable Screen parent) {
		this.parent = parent;
	}

	@Override
	public void tick() {
		super.tick();
		cogSpin.tick();
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
	}

	@Override
	protected void renderWindowBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		if (this.minecraft.level != null) {
			//in game
			graphics.fill(0, 0, this.width, this.height, 0xb0_282c34);
		} else {
			//in menus
			renderMenuBackground(graphics, partialTicks);
		}

		shadowElement
			.at(width * 0.5f, height * 0.5f, 0)
			.render(graphics);

		super.renderWindowBackground(graphics, mouseX, mouseY, partialTicks);

	}

	@Override
	protected void prepareFrame() {
		GlStateManager._clear(GL30.GL_STENCIL_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		cogSpin.bump(3, -scrollY * 5);

		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	@Override
	public boolean isPauseScreen() {
		return true;
	}

	public static String toHumanReadable(String key) {
		String s = key.replaceAll("_", " ");
		s = Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(s)).map(StringUtils::capitalize).collect(Collectors.joining(" "));
		s = StringUtils.normalizeSpace(s);
		return s;
	}

	/**
	 * By default, ConfigScreens will render the Vanilla Panorama as
	 * their background when not opened ingame.
	 * If your mod wants to render something else, please add to the
	 * {@code backgrounds} Map in this Class with your modID as the key.
	 */
	protected void renderMenuBackground(GuiGraphics graphics, float partialTicks) {
		TriConsumer<Screen, GuiGraphics, Float> customBackground = backgrounds.get(modID);
		if (customBackground != null) {
			customBackground.accept(this, graphics, partialTicks);
			return;
		}

		Minecraft.getInstance()
			.gameRenderer
			.getPanorama()
			.render(graphics, this.width, this.height, true);

		graphics.fill(0, 0, this.width, this.height, 0x90_282c34);
	}

	protected static void renderCog(GuiGraphics graphics) {
		float partialTicks = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
		Matrix3x2fStack poseStack = graphics.pose();
		poseStack.pushMatrix();

		poseStack.translate(-100, 100);
		poseStack.scale(200, 200);
		GuiGameElement.of(shadowState)
			.rotateBlock(22.5, cogSpin.getValue(partialTicks), 22.5)
			.render(graphics);

		poseStack.popMatrix();
	}
}
