package net.createmod.catnip.registration.builder;

import net.createmod.catnip.registration.CatnipRegistry;
import net.createmod.catnip.registration.holder.EntityHolder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Function;

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
