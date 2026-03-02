package net.createmod.catnip.api.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;

import net.createmod.catnip.api.animation.LerpedFloat;
import net.createmod.catnip.api.client.gui.element.BoxElement;
import net.createmod.catnip.api.client.gui.texture.CatnipGuiTextures;
import net.createmod.catnip.api.client.gui.widget.BoxWidget;
import net.createmod.catnip.api.data.Couple;
import net.createmod.catnip.api.lang.Lang;
import net.createmod.catnip.api.theme.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public abstract class NavigatableSimiScreen extends AbstractSimiScreen {
	public static final Couple<Color> COLOR_NAV_ARROW = Couple.create(
		new Color(0x80_aa9999, true),
		new Color(0x30_aa9999)
	).map(Color::setImmutable);

	protected static boolean currentlyRenderingPreviousScreen = false;

	protected int depthPointX, depthPointY;
	public final LerpedFloat transition = LerpedFloat.linear()
		.startWithValue(0)
		.chase(0, .1f, LerpedFloat.Chaser.LINEAR);
	protected final LerpedFloat arrowAnimation = LerpedFloat.linear()
		.startWithValue(0)
		.chase(0, 0.075f, LerpedFloat.Chaser.LINEAR);
	@Nullable
	protected BoxWidget backTrack;

	protected NavigatableSimiScreen(Component title) {
		super(title);
		Window window = Minecraft.getInstance().getWindow();
		depthPointX = window.getGuiScaledWidth() / 2;
		depthPointY = window.getGuiScaledHeight() / 2;
	}

	@Override
	public void onClose() {
		ScreenOpener.clearStack();
		super.onClose();
	}

	@Override
	public void tick() {
		super.tick();
		transition.tickChaser();
		arrowAnimation.tickChaser();
	}

	@Override
	protected void init() {
		super.init();

		backTrack = null;
		List<Screen> screenHistory = ScreenOpener.getScreenHistory();
		if (screenHistory.isEmpty())
			return;

		backTrack = addRenderableWidget(new BoxWidget(31, height - 31 - 20)
			.withBounds(20, 20)
			.withCustomBackground(BoxElement.COLOR_BACKGROUND_FLAT)
			.enableFade(0, 5)
			.withPadding(2, 2)
			.fade(1)
			.withCallback(() -> ScreenOpener.openPreviousScreen(this, null)));

		Screen previousScreen = screenHistory.get(0);
		if (previousScreen instanceof NavigatableSimiScreen screen) {
			screen.initBackTrackIcon(backTrack);
		} else {
			backTrack.showing(CatnipGuiTextures.ICON_DISABLE);
		}

	}

	/**
	 * Called when {@code this} represents the previous screen to
	 * initialize the {@code backTrack} icon of the current screen.
	 *
	 * @param backTrack The backTrack button of the current screen.
	 */
	protected abstract void initBackTrackIcon(BoxWidget backTrack);

	protected @Nullable Component backTrackingComponent() {
		if (ScreenOpener.getBackStepScreen() instanceof NavigatableSimiScreen) {
			return Lang.builder("catnip")
				.translate("gui.step_back")
				.component();
		}

		return Lang.builder("catnip")
			.translate("gui.exit")
			.component();
	}

	@Override
	public final void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		// apply transition scaling
		float progress = transition.getValue(partialTicks);
		float scale = 1 - 0.5f * (1 - progress);

		Matrix3x2fStack transforms = graphics.pose();
		transforms.pushMatrix();
		transforms.translate(this.depthPointX, this.depthPointY);
		transforms.scale(scale, scale);
		transforms.translate(-this.depthPointX, -this.depthPointY);

		this.renderScaled(graphics, mouseX, mouseY, partialTicks);

		transforms.popMatrix();
	}

	public void renderScaled(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.render(graphics, mouseX, mouseY, partialTicks);

		if (this.backTrack != null) {
			int x = (int) Mth.lerp(arrowAnimation.getValue(partialTicks), -9, 21);
			int maxX = backTrack.getX() + backTrack.getWidth();
			Couple<Color> colors = COLOR_NAV_ARROW;

			if (x + 30 < backTrack.getX())
				UIRenderHelper.breadcrumbArrow(graphics, x + 30, height - 51, maxX - (x + 30), 20, 5, colors);

			UIRenderHelper.breadcrumbArrow(graphics, x, height - 51, 30, 20, 5, colors);
			UIRenderHelper.breadcrumbArrow(graphics, x - 30, height - 51, 30, 20, 5, colors);

			if (backTrack.isHoveredOrFocused()) {
				Component component = backTrackingComponent();
				graphics.drawString(font, component, 41 - font.width(component) / 2, height - 16, UIRenderHelper.COLOR_TEXT_DARKER.getFirst().getRGB(), false);
				if (Mth.equal(arrowAnimation.getValue(), arrowAnimation.getChaseTarget())) {
					arrowAnimation.setValue(1);
					arrowAnimation.setValue(1);// called twice to also set the previous value to 1
				}
			}
		}
	}

	@Override
	public boolean keyPressed(KeyEvent keyEvent) {
		if (keyEvent.key() == InputConstants.KEY_BACKSPACE) {
			ScreenOpener.openPreviousScreen(this, null);
			return true;
		}
		return super.keyPressed(keyEvent);
	}

	public void centerScalingOn(int x, int y) {
		depthPointX = x;
		depthPointY = y;
	}

	public void centerScalingOnMouse() {
		Window w = minecraft.getWindow();
		double mouseX = minecraft.mouseHandler.xpos() * w.getGuiScaledWidth() / w.getScreenWidth();
		double mouseY = minecraft.mouseHandler.ypos() * w.getGuiScaledHeight() / w.getScreenHeight();
		centerScalingOn((int) mouseX, (int) mouseY);
	}

	public boolean isEquivalentTo(NavigatableSimiScreen other) {
		return false;
	}

	public void shareContextWith(NavigatableSimiScreen other) {
	}

	protected void renderZeloBreadcrumbs(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		List<Screen> history = ScreenOpener.getScreenHistory();
		if (history.isEmpty())
			return;

		history.add(0, minecraft.screen);
		int spacing = 20;

		List<String> names = new ArrayList<>();
		for (Screen screen : history)
			names.add(NavigatableSimiScreen.screenTitle(screen));

		int bWidth = 0;
		for (String name : names) {
			bWidth += font.width(name) + spacing;
		}

		MutableInt x = new MutableInt(width - bWidth);
		MutableInt y = new MutableInt(height - 18);
		MutableBoolean first = new MutableBoolean(true);

		if (x.intValue() < 25)
			x.setValue(25);

		names.forEach(s -> {
			int sWidth = font.width(s);
			UIRenderHelper.breadcrumbArrow(graphics, x.intValue(), y.intValue(), sWidth + spacing, 14, spacing / 2,
				new Color(0xdd101010), new Color(0x44101010));
			graphics.drawString(font, s, x.intValue() + 5, y.intValue() + 3, first.get() ? 0xffeeffee : 0xffddeeff);
			first.setFalse();

			x.add(sWidth + spacing);
		});
	}

	public static boolean isCurrentlyRenderingPreviousScreen() {
		return currentlyRenderingPreviousScreen;
	}

	private static String screenTitle(Screen screen) {
		if (screen instanceof NavigatableSimiScreen)
			return ((NavigatableSimiScreen) screen).getBreadcrumbTitle();
		return "<";
	}

	protected String getBreadcrumbTitle() {
		return this.getClass()
			.getSimpleName();
	}
}
