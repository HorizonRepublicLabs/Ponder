package net.createmod.ponder.mixin.client.accessor;

import java.util.Map;

import net.minecraft.resources.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;

@Mixin(ParticleEngine.class)
public interface ParticleEngineAccessor {
	// This field cannot be ATed because its type is patched by Forge
	@Accessor("providers")
	Map<Identifier, ParticleProvider<?>> ponder$getProviders();
}
