package net.createmod.catnip.api.client.gui.element;

import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.opengl.GlStateManager;

import net.minecraft.client.gui.GuiGraphics;

public interface StencilElement extends RenderElement {
	@Override
	default void render(GuiGraphics graphics) {
		graphics.pose().pushMatrix();
		transform(graphics);
		prepareStencil(graphics);
		renderStencil(graphics);
		prepareElement(graphics);
		renderElement(graphics);
		cleanUp(graphics);
		graphics.pose().popMatrix();
	}

	void renderStencil(GuiGraphics graphics);

	void renderElement(GuiGraphics graphics);

	default void transform(GuiGraphics graphics) {
		graphics.pose().translate(getX(), getY());
	}

	default void prepareStencil(GuiGraphics graphics) {
		//graphics.flush(); TODO - Is there an replacement?
		GL30.glDisable(GL30.GL_STENCIL_TEST);
		GL30.glStencilMask(~0);
		GlStateManager._clear(GL30.GL_STENCIL_BUFFER_BIT);
		GL30.glEnable(GL30.GL_STENCIL_TEST);
		GL30.glStencilOp(GL30.GL_REPLACE, GL30.GL_KEEP, GL30.GL_KEEP);
		GL30.glStencilMask(0xFF);
		GL30.glStencilFunc(GL30.GL_NEVER, 1, 0xFF);
	}

	default void prepareElement(GuiGraphics graphics) {
		GL30.glEnable(GL30.GL_STENCIL_TEST);
		GL30.glStencilOp(GL30.GL_KEEP, GL30.GL_KEEP, GL30.GL_KEEP);
		GL30.glStencilFunc(GL30.GL_EQUAL, 1, 0xFF);
	}

	default void cleanUp(GuiGraphics graphics) {
		GL30.glDisable(GL30.GL_STENCIL_TEST);
		//graphics.flush(); TODO - Is there an replacement?
	}
}
