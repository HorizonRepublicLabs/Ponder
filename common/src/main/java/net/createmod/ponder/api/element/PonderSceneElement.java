package net.createmod.ponder.api.element;

import com.mojang.blaze3d.vertex.PoseStack;

import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.rendertype.RenderType;

public interface PonderSceneElement extends PonderElement {

	void renderFirst(PonderLevel world, MultiBufferSource buffer, GuiGraphics graphics, PoseStack poseStack, float pt);

	void renderLayer(PonderLevel world, MultiBufferSource buffer, ChunkSectionLayer layer, GuiGraphics graphics, PoseStack poseStack, float pt);

	void renderLast(PonderLevel world, MultiBufferSource buffer, GuiGraphics graphics, PoseStack poseStack, float pt);

}
