package net.createmod.catnip.registration.builder;

import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.createmod.catnip.registration.CatnipRegistry;
import net.createmod.catnip.registration.holder.BlockEntityHolder;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BlockEntityBuilder<T extends BlockEntity> extends AbstractBuilder<BlockEntityType<?>, BlockEntityType<T>, BlockEntityHolder<T>> {
	private final BlockEntityFactory<T> factory;
	private final Set<Supplier<@NotNull Block>> validBlocks = new HashSet<>();

	public BlockEntityBuilder(CatnipRegistry owner, String name, BlockEntityFactory<T> factory) {
		super(owner, name, BuiltInRegistries.BLOCK_ENTITY_TYPE);
		this.factory = factory;
	}

	@SafeVarargs
	public final BlockEntityBuilder<T> validBlocks(Supplier<@NotNull Block>... blocks) {
		return validBlocks(List.of(blocks));
	}

	public BlockEntityBuilder<T> validBlocks(List<Supplier<@NotNull Block>> blocks) {
		validBlocks.addAll(blocks);
		return this;
	}

	// TODO - ClientOnly
	public BlockEntityBuilder<T> renderer(Supplier<@NotNull BlockEntityRendererProvider<T>> renderer) {
		chainAfterRegisterCallback(holder -> BlockEntityRenderers.register(holder.value(), renderer.get()));
		return this;
	}

	// TODO - ClientOnly
	public BlockEntityBuilder<T> visualizer(Supplier<SimpleBlockEntityVisualizer.@NotNull Factory<T>> visualizer) {
		return visualizer(visualizer, be -> true);
	}

	// TODO - ClientOnly
	public BlockEntityBuilder<T> visualizer(Supplier<SimpleBlockEntityVisualizer.@NotNull Factory<T>> visualizer, boolean skipVanillaRenderer) {
		return visualizer(visualizer, be -> skipVanillaRenderer);
	}

	// TODO - ClientOnly
	public BlockEntityBuilder<T> visualizer(Supplier<SimpleBlockEntityVisualizer.@NotNull Factory<T>> visualizer, Predicate<T> skipVanillaRender) {
		chainAfterRegisterCallback(holder ->
			SimpleBlockEntityVisualizer.builder(holder.value())
				.factory(visualizer.get())
				.skipVanillaRender(skipVanillaRender)
				.apply()
		);
		return this;
	}

	@Override
	BlockEntityType<T> build() {
		Set<Block> validBlocks = this.validBlocks.stream()
			.map(Supplier::get)
			.collect(Collectors.toSet());
		//noinspection DataFlowIssue
		return new BlockEntityType<>((p, s) -> factory.createBlockEntity(holder.value(), p, s), validBlocks, null);
	}

	@Override
	BlockEntityHolder<T> getHolder(HolderOwner<BlockEntityType<?>> owner, ResourceKey<BlockEntityType<?>> key) {
		//noinspection unchecked,rawtypes
		return new BlockEntityHolder<>((HolderOwner) owner, (ResourceKey) key);
	}

	@FunctionalInterface
	public interface BlockEntityFactory<T extends BlockEntity> {
		T createBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state);
	}
}
