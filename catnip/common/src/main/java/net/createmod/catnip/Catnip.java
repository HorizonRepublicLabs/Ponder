package net.createmod.catnip;

import net.minecraft.resources.Identifier;

public final class Catnip {
	public static final String ID = "catnip";

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(ID, path);
	}
}
