package net.createmod.catnip.platform.services;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

public interface ModFluidHelper {
	int getColor(Fluid fluid, long amount, @Nullable DataComponentPatch fluidData);

	int getLuminosity(Fluid fluid, long amount, @Nullable DataComponentPatch fluidData);

	ResourceLocation getStillTexture(Fluid fluid, long amount, @Nullable DataComponentPatch fluidData);

	boolean isLighterThanAir(Fluid fluid, @Nullable DataComponentPatch fluidData);
}
