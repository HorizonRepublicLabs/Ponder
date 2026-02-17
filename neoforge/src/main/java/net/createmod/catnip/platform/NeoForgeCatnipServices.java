package net.createmod.catnip.platform;

import net.createmod.catnip.api.client.render.FluidRenderHelper;
import net.createmod.catnip.api.platform.CatnipServices;
import net.createmod.catnip.api.platform.services.ModFluidHelper;

import net.neoforged.neoforge.fluids.FluidStack;

@SuppressWarnings("unchecked")
public class NeoForgeCatnipServices {
	public static final ModFluidHelper<FluidStack> FLUID_HELPER = (ModFluidHelper<FluidStack>) CatnipServices.FLUID_HELPER;
	public static final FluidRenderHelper<FluidStack> FLUID_RENDERER = (FluidRenderHelper<FluidStack>) CatnipServices.FLUID_RENDERER;
}
