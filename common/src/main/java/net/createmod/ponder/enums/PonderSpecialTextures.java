package net.createmod.ponder.enums;

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
	public Identifier getId() {
		return identifier;
	}
}
