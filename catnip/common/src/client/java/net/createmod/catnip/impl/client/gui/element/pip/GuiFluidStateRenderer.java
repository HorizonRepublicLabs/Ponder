package net.createmod.catnip.impl.client.gui.element.pip;

import com.mojang.blaze3d.vertex.PoseStack;

import net.createmod.catnip.api.client.gui.render.pip.GuiFluidStateRenderState;
import net.createmod.catnip.api.client.platform.ModClientHooksHelper;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;

public class GuiFluidStateRenderer extends PictureInPictureRenderer<GuiFluidStateRenderState> {
	public GuiFluidStateRenderer(BufferSource bufferSource) {
		super(bufferSource);
	}

	@Override
	public Class<GuiFluidStateRenderState> getRenderStateClass() {
		return GuiFluidStateRenderState.class;
	}

	@Override
	protected void renderToTexture(GuiFluidStateRenderState renderState, PoseStack poseStack) {
		ModClientHooksHelper.INSTANCE.renderFullFluidState(poseStack, this.bufferSource, renderState.fluidState());
	}

	@Override
	protected String getTextureLabel() {
		return "catnip:gui_fluid_state";
	}
}
