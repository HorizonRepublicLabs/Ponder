package net.createmod.catnip.render;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.resources.Identifier;

public interface BindableTexture {
	default void bind() {
		RenderSystem.setShaderTexture(0, getId());
	}

	Identifier getId();
}
