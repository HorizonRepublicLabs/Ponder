package net.createmod.ponder.api.client.element;

import net.createmod.ponder.api.client.scene.PonderScene;
import net.createmod.ponder.impl.client.gui.PonderUI;
import net.minecraft.client.gui.GuiGraphics;

public interface PonderOverlayElement extends PonderElement {

	void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks);

}
