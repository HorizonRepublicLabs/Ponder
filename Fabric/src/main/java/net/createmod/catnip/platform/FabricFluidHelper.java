package net.createmod.catnip.platform;

import org.jetbrains.annotations.Nullable;

import net.createmod.catnip.platform.services.ModFluidHelper;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

public class FabricFluidHelper implements ModFluidHelper {
	@Override
	public int getColor(Fluid fluid, long amount, @Nullable DataComponentPatch fluidData) {
		return FluidVariantRendering.getColor(toVariant(fluid, fluidData));
	}

	@Override
	public int getLuminosity(Fluid fluid, long amount, @Nullable DataComponentPatch fluidData) {
		return FluidVariantAttributes.getLuminance(toVariant(fluid, fluidData));
	}

	@Override
	public ResourceLocation getStillTexture(Fluid fluid, long amount, @Nullable DataComponentPatch fluidData) {
		return FluidVariantRendering.getSprite(toVariant(fluid, fluidData)).atlasLocation();
	}

	@Override
	public boolean isLighterThanAir(Fluid fluid, @Nullable DataComponentPatch fluidData) {
		return FluidVariantAttributes.isLighterThanAir(toVariant(fluid, fluidData));
	}

	private FluidVariant toVariant(Fluid fluid, @Nullable DataComponentPatch fluidData) {
		return FluidVariant.of(fluid, fluidData == null ? DataComponentPatch.EMPTY : fluidData);
	}
}
