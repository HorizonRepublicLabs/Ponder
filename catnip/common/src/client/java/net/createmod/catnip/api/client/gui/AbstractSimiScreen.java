package net.createmod.catnip.api.client.gui;

import java.util.List;

import net.createmod.catnip.impl.client.mixin.ScreenAccessor;
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
