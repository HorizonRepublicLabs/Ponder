package net.createmod.catnip.levelWrappers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.LevelAccessor;

public class WorldHelper {
	public static Identifier getDimensionID(LevelAccessor world) {
		return world.registryAccess()
			.getOrThrow(Registries.DIMENSION_TYPE)
			.value()
			.getKey(world.dimensionType());
	}
}
