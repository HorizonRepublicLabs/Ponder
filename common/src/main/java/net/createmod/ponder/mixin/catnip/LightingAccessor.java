package net.createmod.ponder.mixin.catnip;

import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.blaze3d.platform.Lighting;

@Mixin(Lighting.class)
public interface LightingAccessor {
	@Invoker
	void callUpdateBuffer(Lighting.Entry entry, Vector3f light0, Vector3f light1);
}
