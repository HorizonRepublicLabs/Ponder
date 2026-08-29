package net.createmod.catnip.api.client.gui;

import java.util.List;

import net.createmod.catnip.impl.client.mixin.ScreenAccessor;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public abstract class AbstractSimiScreen extends Screen implements CatnipScreenExtensions {
	/// The window layout the reorganisation dropped. Screens across catnip,
	/// ponder and Create centre themselves on these, and set the size before
	/// calling super.init().
	protected int windowWidth, windowHeight;
	protected int windowXOffset, windowYOffset;
	protected int guiLeft, guiTop;

	protected AbstractSimiScreen() {
		this(CommonComponents.EMPTY);
	}

	protected AbstractSimiScreen(Component title) {
		super(title);
	}

	/// Must be called before super.init().
	protected void setWindowSize(int width, int height) {
		windowWidth = width;
		windowHeight = height;
	}

	/// Must be called before super.init().
	protected void setWindowOffset(int xOffset, int yOffset) {
		windowXOffset = xOffset;
		windowYOffset = yOffset;
	}

	@Override
	protected void init() {
		super.init();
		guiLeft = (width - windowWidth) / 2 + windowXOffset;
		guiTop = (height - windowHeight) / 2 + windowYOffset;
	}

	/// Screens extract render state rather than draw directly in 26.x. This
	/// keeps the renderWindow hook the catnip, ponder and Create screens are
	/// written against, running it after the vanilla extraction.
	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
		renderWindow(graphics, mouseX, mouseY, partialTicks);
		renderWindowForeground(graphics, mouseX, mouseY, partialTicks);
	}

	protected void renderWindow(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {}

	protected void renderWindowForeground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean shouldCloseOnE() {
		return true;
	}

	protected final List<Renderable> getRenderables() {
		return ((ScreenAccessor) this).catnip$getRenderables();
	}
}
