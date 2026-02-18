package net.createmod.catnip.api;

import net.createmod.catnip.impl.network.CatnipPayloads;
import net.minecraft.resources.Identifier;

public final class Catnip {
	public static final String ID = "catnip";

	public static void init() {
		CatnipPayloads.init();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(ID, path);
	}
}
