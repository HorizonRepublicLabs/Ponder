package net.createmod.ponder.api.client.element;

import net.createmod.ponder.api.client.scene.PonderScene;
import net.createmod.ponder.impl.client.gui.PonderUI;
import net.minecraft.client.gui.GuiGraphics;

public interface AnimatedOverlayElement extends PonderOverlayElement {

	void setFade(float fade);

	float getFade(float partialTicks);

	@Override
	default void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks) {
		render(scene, screen, graphics, partialTicks, getFade(partialTicks));
	}

	void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks, float fade);
}
