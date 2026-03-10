package net.createmod.ponder.impl.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.texture.TextureAtlas;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
	@Accessor
	TextureAtlas getGuiSprites();
}
