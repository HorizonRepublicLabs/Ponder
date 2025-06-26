package net.createmod.catnip.registration.builder;

import net.createmod.catnip.registration.CatnipRegistry;
import net.createmod.catnip.registration.holders.BlockHolder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder<T extends Block> extends AbstractBuilder<Block, T, BlockHolder<T>> {
	private final Function<Properties, T> blockFunc;
	private Supplier<Properties> initialProperties = Properties::of;
	private Function<Properties, Properties> properties = Function.identity();

	public BlockBuilder(CatnipRegistry owner, String name, Function<Properties, T> blockFunc) {
		super(owner, name, BuiltInRegistries.BLOCK);
		this.blockFunc = blockFunc;
	}

	public BlockBuilder<T> initialProperties(Supplier<T> block) {
		initialProperties = () -> Properties.ofFullCopy(block.get());
		return this;
	}

	public BlockBuilder<T> properties(Function<Properties, Properties> func) {
		properties = properties.andThen(func);
		return this;
	}

	@Override
	T build() {
		Properties properties = initialProperties.get();
		properties = this.properties.apply(properties);
		return blockFunc.apply(properties);
	}

	@SuppressWarnings("unchecked")
	@Override
	BlockHolder<T> getHolder(HolderOwner<Block> owner, ResourceKey<Block> key) {
		return (BlockHolder<T>) new BlockHolder<>(owner, key);
	}
}
