package net.createmod.catnip.gui.texture;

import net.createmod.catnip.Catnip;
import net.createmod.catnip.render.BindableTexture;
import net.minecraft.resources.Identifier;

public enum CatnipSpecialTextures implements BindableTexture {
	BLANK("blank.png");

	public static final String ASSET_PATH = "textures/special/";
	private final Identifier identifier;

	CatnipSpecialTextures(String filename) {
		identifier = Catnip.id(ASSET_PATH + filename);
	}

	@Override
	public Identifier getId() {
		return identifier;
	}
}
