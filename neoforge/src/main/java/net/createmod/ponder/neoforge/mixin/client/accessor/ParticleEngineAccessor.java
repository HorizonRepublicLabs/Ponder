package net.createmod.ponder.neoforge.mixin.client.accessor;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.resources.Identifier;

@Mixin(ParticleEngine.class)
public interface ParticleEngineAccessor {
	// This field cannot be ATed because its type is patched by Forge
	@Accessor("providers")
	Map<Identifier, ParticleProvider<?>> ponder$getProviders();
}
