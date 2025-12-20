package net.createmod.ponder.api.element;

import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public interface PonderSceneElement extends PonderElement {

	void renderFirst(PonderLevel world, MultiBufferSource buffer, GuiGraphics graphics, float pt);

	void renderLayer(PonderLevel world, MultiBufferSource buffer, ChunkSectionLayer layer, GuiGraphics graphics, float pt);

	void renderLast(PonderLevel world, MultiBufferSource buffer, GuiGraphics graphics, float pt);

}
