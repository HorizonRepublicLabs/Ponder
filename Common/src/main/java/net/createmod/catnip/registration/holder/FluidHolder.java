package net.createmod.catnip.registration.holder;

import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

import org.jetbrains.annotations.Nullable;

public class FluidHolder<T extends Fluid> extends BaseHolder<T> {
	protected FluidHolder(HolderOwner<T> owner, ResourceKey<T> key) {
		super(owner, key);
	}

	@Override
	public <V> boolean is(V value) {
		return value().isSame((Fluid) value);
	}

	// TODO - getSource(), getType(), getBlock(), getBucket()
}
