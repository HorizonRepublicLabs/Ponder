package net.createmod.ponder.enums;

import com.mojang.blaze3d.systems.RenderSystem;

import net.createmod.catnip.render.BindableTexture;
import net.createmod.ponder.Ponder;
import net.minecraft.resources.Identifier;

public enum PonderSpecialTextures implements BindableTexture {
	BLANK("blank.png");

	public static final String ASSET_PATH = "textures/special/";
	private final Identifier identifier;

	PonderSpecialTextures(String filename) {
		identifier = Ponder.id(ASSET_PATH + filename);
	}

	@Override
	public void bind() {
		RenderSystem.setShaderTexture(0, identifier);
	}

	@Override
	public Identifier getId() {
		return identifier;
	}
}
