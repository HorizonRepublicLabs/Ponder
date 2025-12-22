package net.createmod.catnip.gui.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;

import org.jspecify.annotations.Nullable;

public record PonderSceneRenderState() implements GuiElementRenderState {
	@Override
	public RenderPipeline pipeline() {
		return null;
	}

	@Override
	public void buildVertices(VertexConsumer consumer) {

	}

	@Override
	public TextureSetup textureSetup() {
		return null;
	}

	@Override
	public @Nullable ScreenRectangle scissorArea() {
		return null;
	}

	@Override
	public @Nullable ScreenRectangle bounds() {
		return null;
	}
}
