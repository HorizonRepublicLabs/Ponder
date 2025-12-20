package net.createmod.catnip.gui;

import com.mojang.blaze3d.platform.Lighting.Entry;

import net.minecraft.client.Minecraft;

@FunctionalInterface
public interface ILightingSettings {
	ILightingSettings DEFAULT_3D = setupFor(Entry.ITEMS_3D);
	ILightingSettings DEFAULT_FLAT = setupFor(Entry.ITEMS_FLAT);

	void applyLighting();

	private static ILightingSettings setupFor(Entry entry) {
		return () -> Minecraft.getInstance().gameRenderer.getLighting().setupFor(entry);
	}
}
