package net.createmod.catnip.platform.services;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;

// FIXME: some of these methods are client-only and not marked at runtime. This will cause problems later!
public interface ModFluidHelper<R> {
	/**
	 * <strong>Client-only! Calling this server-side will crash.</strong>
	 */
	int getColor(R fluid, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);

	int getLuminosity(R fluid);

	/**
	 * <strong>Client-only! Calling this server-side will crash.</strong>
	 */
	@Nullable
	TextureAtlasSprite getStillTexture(R fluid);

	/**
	 * <strong>Client-only! Calling this server-side will crash.</strong>
	 */
	default TextureAtlasSprite getStillTextureOrMissing(R fluid) {
		TextureAtlasSprite texture = this.getStillTexture(fluid);
		if (texture != null)
			return texture;

		return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation());
	}

	boolean isLighterThanAir(R fluid);

	R toStack(FluidState state);
}
