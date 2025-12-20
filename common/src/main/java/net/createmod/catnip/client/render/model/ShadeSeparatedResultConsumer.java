package net.createmod.catnip.client.render.model;

import com.mojang.blaze3d.vertex.MeshData;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.rendertype.RenderType;

public interface ShadeSeparatedResultConsumer {
	void accept(ChunkSectionLayer layer, boolean shaded, MeshData data);
}
