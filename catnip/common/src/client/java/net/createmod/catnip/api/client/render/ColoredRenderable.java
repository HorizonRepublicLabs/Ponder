package net.createmod.catnip.api.client.render;

import net.createmod.catnip.api.theme.Color;
import net.minecraft.client.gui.GuiGraphics;

public interface ColoredRenderable {
	void render(GuiGraphics graphics, int x, int y, Color c);
}
