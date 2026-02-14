package net.createmod.catnip.registration.builder;

import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import dev.engine_room.flywheel.lib.visualization.SimpleEntityVisualizer;
import net.createmod.catnip.annotations.ClientOnly;
import net.createmod.catnip.registration.CatnipRegistry;
import net.createmod.catnip.registration.holder.EntityHolder;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EntityBuilder<T extends Entity> extends AbstractBuilder<EntityType<?>, EntityType<T>, EntityHolder<T>> {
	private final EntityType.Builder<T> builder;
	private Function<EntityType.Builder<T>, EntityType.Builder<T>> properties = Function.identity();

	public EntityBuilder(CatnipRegistry owner, String name, Registry<EntityType<?>> registry, EntityType.EntityFactory<T> factory, MobCategory category) {
		super(owner, name, registry);
		this.builder = EntityType.Builder.of(factory, category);
	}

	public EntityBuilder<T> properties(Function<EntityType.Builder<T>, EntityType.Builder<T>> properties) {
		this.properties = this.properties.andThen(properties);
		return this;
	}

	@ClientOnly
	public EntityBuilder<T> renderer(Supplier<EntityRendererProvider<T>> renderer) {
		chainAfterRegisterCallback(holder -> EntityRenderers.register(holder.value(), renderer.get()));
		return this;
	}

	@ClientOnly
	public EntityBuilder<T> visualizer(Supplier<SimpleEntityVisualizer.Factory<T>> visualizer) {
		return visualizer(visualizer, be -> true);
	}

	@ClientOnly
	public EntityBuilder<T> visualizer(Supplier<SimpleEntityVisualizer.Factory<T>> visualizer, boolean skipVanillaRenderer) {
		return visualizer(visualizer, be -> skipVanillaRenderer);
	}

	@ClientOnly
	public EntityBuilder<T> visualizer(Supplier<SimpleEntityVisualizer.Factory<T>> visualizer, Predicate<T> skipVanillaRender) {
		chainAfterRegisterCallback(holder ->
			SimpleEntityVisualizer.builder(holder.value())
				.factory(visualizer.get())
				.skipVanillaRender(skipVanillaRender)
				.apply()
		);
		return this;
	}

	// TODO - attributes, all the other entity-y things

	@Override
	EntityType<T> build() {
		EntityType.Builder<T> builder = this.builder;
		builder = properties.apply(builder);
		return builder.build(id.toString());
	}

	@Override
	EntityHolder<T> getHolder(HolderOwner<EntityType<?>> owner, ResourceKey<EntityType<?>> key) {
		//noinspection unchecked,rawtypes
		return new EntityHolder<>((HolderOwner) owner, (ResourceKey) key);
	}
}
