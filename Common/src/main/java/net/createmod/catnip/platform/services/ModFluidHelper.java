package net.createmod.catnip.platform.services;

import net.createmod.catnip.annotations.Environment;

import net.createmod.catnip.annotations.Environment.EnvType;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;

public interface ModFluidHelper<R> {
	@Environment(EnvType.CLIENT)
	int getColor(R fluid, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);

	int getLuminosity(R fluid);

	@Environment(EnvType.CLIENT)
	@Nullable
	TextureAtlasSprite getStillTexture(R fluid);

	@Environment(EnvType.CLIENT)
	default TextureAtlasSprite getStillTextureOrMissing(R fluid) {
		TextureAtlasSprite texture = this.getStillTexture(fluid);
		if (texture != null)
			return texture;

		return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation());
	}

	boolean isLighterThanAir(R fluid);

	R toStack(FluidState state);
}
