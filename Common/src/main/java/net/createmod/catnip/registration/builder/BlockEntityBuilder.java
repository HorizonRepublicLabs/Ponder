package net.createmod.catnip.registration.builder;

import net.createmod.catnip.registration.CatnipRegistry;
import net.createmod.catnip.registration.holder.BlockEntityHolder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityBuilder<T extends BlockEntity> extends AbstractBuilder<BlockEntityType<?>, BlockEntityType<?>, BlockEntityHolder<T>> {
	public BlockEntityBuilder(CatnipRegistry owner, String name) {
		super(owner, name, BuiltInRegistries.BLOCK_ENTITY_TYPE);
	}

	@Override
	BlockEntityType<T> build() {
		return null;
	}

	@Override
	BlockEntityHolder<T> getHolder(HolderOwner<BlockEntityType<?>> owner, ResourceKey<BlockEntityType<?>> key) {
		return new BlockEntityHolder<>(owner, key);
	}
}
