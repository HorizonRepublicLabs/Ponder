package net.createmod.catnip.impl.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
	@Invoker("getFov")
	float catnip$callGetFov(Camera camera, float partialTick, boolean useFovSetting);
}
